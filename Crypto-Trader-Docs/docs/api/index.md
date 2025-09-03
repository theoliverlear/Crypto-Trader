# API

The full OpenAPI specification for Crypto Trader is embedded below.

<div id="redoc"></div>

<script src="https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function() {
    Redoc.init('../openapi.yaml', {
      hideHostname: true,
      expandResponses: '200,201',
      pathInMiddlePanel: true,
      theme: {
        spacing: { sectionVertical: 12 },
        colors: { primary: { main: '#3f51b5' } },
        typography: { fontSize: '14px' }
      }
    }, document.getElementById('redoc'));
  });
</script>
