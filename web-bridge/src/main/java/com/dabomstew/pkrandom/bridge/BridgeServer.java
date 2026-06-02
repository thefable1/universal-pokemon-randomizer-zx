package com.dabomstew.pkrandom.bridge;

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.RandomSource;
import com.dabomstew.pkrandom.Randomizer;
import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.Version;
import com.dabomstew.pkrandom.romhandlers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A minimal headless HTTP bridge over the randomizer engine -- the integration
 * layer a Tauri+Svelte (or any web) frontend talks to.
 *
 * It uses only the JDK's built-in HTTP server plus Gson, and reuses the engine
 * exactly as the CLI/GUI do: load a ROM via the RomHandler factories, build a
 * Settings, run a Randomizer. No engine code is modified.
 *
 * Endpoints (all JSON, CORS-enabled so a Vite dev server on another port works):
 *   GET  /api/health             -> { status, engineVersion }
 *   POST /api/settings/encode    -> body: SettingsRequest; returns the engine
 *                                   settings string (proves the JSON<->engine
 *                                   contract; needs no ROM)
 *   POST /api/randomize          -> body: { romPath, outputPath, settings };
 *                                   loads the ROM, randomizes, returns the log
 */
public class BridgeServer {

    private static final int DEFAULT_PORT = 7890;
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("com/dabomstew/pkrandom/newgui/Bundle");

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);

        server.createContext("/api/health", withCors(BridgeServer::health));
        server.createContext("/api/settings/encode", withCors(BridgeServer::encode));
        server.createContext("/api/randomize", withCors(BridgeServer::randomize));

        server.setExecutor(null);
        server.start();
        System.out.println("Randomizer bridge listening on http://127.0.0.1:" + port);
    }

    // --- Endpoints -----------------------------------------------------------

    private static void health(HttpExchange ex) throws IOException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "ok");
        body.put("engineVersion", Version.VERSION_STRING);
        body.put("settingsFormatVersion", Version.VERSION);
        sendJson(ex, 200, body);
    }

    private static void encode(HttpExchange ex) throws IOException {
        if (!"POST".equals(ex.getRequestMethod())) {
            sendError(ex, 405, "Use POST");
            return;
        }
        SettingsRequest req = readBody(ex, SettingsRequest.class);
        Settings settings = SettingsMapper.toSettings(req);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("settingsString", settings.toString());
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("baseStatsMod", settings.getBaseStatisticsMod().toString());
        summary.put("typesMod", settings.getTypesMod().toString());
        summary.put("wildForceFullyEvolved", settings.isWildForceFullyEvolved());
        summary.put("wildForceFullyEvolvedLevel", settings.getWildForceFullyEvolvedLevel());
        body.put("appliedSummary", summary);
        sendJson(ex, 200, body);
    }

    private static void randomize(HttpExchange ex) throws IOException {
        if (!"POST".equals(ex.getRequestMethod())) {
            sendError(ex, 405, "Use POST");
            return;
        }
        RandomizeRequest req = readBody(ex, RandomizeRequest.class);
        if (req == null || req.romPath == null || req.outputPath == null) {
            sendError(ex, 400, "romPath and outputPath are required");
            return;
        }

        Settings settings = SettingsMapper.toSettings(req.settings != null ? req.settings : new SettingsRequest());
        settings.setCustomNames(FileFunctions.getCustomNames());

        ByteArrayOutputStream logBuffer = new ByteArrayOutputStream();
        PrintStream log = new PrintStream(logBuffer, true, StandardCharsets.UTF_8);

        RomHandler.Factory[] factories = {
                new Gen1RomHandler.Factory(), new Gen2RomHandler.Factory(),
                new Gen3RomHandler.Factory(), new Gen4RomHandler.Factory(),
                new Gen5RomHandler.Factory(), new Gen6RomHandler.Factory(),
                new Gen7RomHandler.Factory()
        };

        File romFile = new File(req.romPath);
        try {
            for (RomHandler.Factory factory : factories) {
                if (factory.isLoadable(romFile.getAbsolutePath())) {
                    RomHandler romHandler = factory.create(RandomSource.instance());
                    romHandler.loadRom(romFile.getAbsolutePath());

                    Randomizer randomizer = new Randomizer(settings, romHandler, BUNDLE, false);
                    randomizer.randomize(req.outputPath, log);
                    log.flush();

                    Map<String, Object> body = new LinkedHashMap<>();
                    body.put("success", true);
                    body.put("game", romHandler.getROMName());
                    body.put("generation", romHandler.generationOfPokemon());
                    body.put("outputPath", req.outputPath);
                    body.put("log", logBuffer.toString(StandardCharsets.UTF_8));
                    sendJson(ex, 200, body);
                    return;
                }
            }
            sendError(ex, 422, "Unsupported ROM: " + romFile.getName());
        } catch (Exception e) {
            sendError(ex, 500, "Randomization failed: " + e.getMessage());
        }
    }

    /** Body shape for /api/randomize. */
    private static class RandomizeRequest {
        String romPath;
        String outputPath;
        SettingsRequest settings;
    }

    // --- Plumbing ------------------------------------------------------------

    /** Wraps a handler with CORS headers and OPTIONS preflight handling. */
    private static HttpHandler withCors(HttpHandler handler) {
        return ex -> {
            ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            if ("OPTIONS".equals(ex.getRequestMethod())) {
                ex.sendResponseHeaders(204, -1);
                ex.close();
                return;
            }
            try {
                handler.handle(ex);
            } catch (Exception e) {
                sendError(ex, 500, e.getMessage());
            }
        };
    }

    private static <T> T readBody(HttpExchange ex, Class<T> type) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, type);
        }
    }

    private static void sendJson(HttpExchange ex, int status, Object body) throws IOException {
        byte[] bytes = GSON.toJson(body).getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendError(HttpExchange ex, int status, String message) {
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", message);
            sendJson(ex, status, body);
        } catch (IOException ignored) {
            // nothing more we can do
        }
    }
}
