# API

The full OpenAPI specification for Crypto Trader is embedded below.

<div class="fullwidth">
  <div id="redoc"></div>
</div>

<script src="https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', async function() {
    const container = document.getElementById('redoc');
    const candidates = [
      'openapi-api.yaml',
      'openapi.yaml',
      'openapi-api.json',
      'openapi.json'
    ];

    async function findSpecUrl() {
      for (const url of candidates) {
        try {
          const res = await fetch(url, { cache: 'no-store' });
          if (res.ok) return url;
        } catch (e) {
          // try next
        }
      }
      return null;
    }

    const specUrl = await findSpecUrl();
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

    if (specUrl) {
      Redoc.init(specUrl, options, container);
    } else {
      console.error('CT API OpenAPI spec not found. Tried: ', candidates);
      container.innerHTML = '<p><strong>OpenAPI spec not found.</strong><br/>Tried: ' + candidates.join(', ') + '</p>';
    }
  });
</script>
