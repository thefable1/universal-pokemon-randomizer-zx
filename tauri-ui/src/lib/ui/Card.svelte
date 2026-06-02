<script lang="ts">
  let { title, children } = $props();
</script>

<section class="card">
  <h3>{title}</h3>
  {@render children()}
</section>

<style>
  .card {
    background: #1a1d27;
    border: 1px solid #272b38;
    border-radius: 12px;
    padding: 14px 16px;
    margin-bottom: 14px;
  }
  h3 { font-size: 0.95rem; margin: 0 0 10px; color: #aab2c5; }
</style>
