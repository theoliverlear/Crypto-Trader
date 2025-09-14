# API

The full OpenAPI specification for Crypto Trader is embedded below.

<div class="fullwidth">
  <div id="redoc"></div>
</div>

<script src="https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const container = document.getElementById('redoc');
    const specUrl = '../api/openapi-api.yaml';
    const options = {
      hideHostname: true,
      expandResponses: '200,201',
      pathInMiddlePanel: true,
      theme: {
        spacing: { sectionVertical: 12 },
        colors: { primary: { main: '#3f51b5' } },
        typography: { fontSize: '14px' }
      }
    };

    // Try to fetch the spec first; if it fails, show a friendly message
    fetch(specUrl, { cache: 'no-store' })
      .then(res => res.ok ? res.text() : Promise.reject(new Error('HTTP ' + res.status)))
      .then(text => {
        if (text && text.trim().length > 0) {
          Redoc.init(specUrl, options, container);
        } else {
          throw new Error('Empty spec file');
        }
      })
      .catch(err => {
        console.error('CT API OpenAPI spec not found at', specUrl, err);
        container.innerHTML = '<p><strong>OpenAPI spec not found.</strong><br/>Expected at: ' + specUrl + '</p>';
      });
  });
</script>
