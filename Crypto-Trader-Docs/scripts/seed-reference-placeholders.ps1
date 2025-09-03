$ErrorActionPreference = "Stop"

$modules = @(
  "Api",
  "Admin",
  "Assets",
  "Data",
  "Engine",
  "Library",
  "Logging",
  "Testing",
  "Version"
)

$root = Join-Path $PSScriptRoot "..\docs\reference"

foreach ($m in $modules) {
  $modDir = Join-Path $root $m
  $apiDir = Join-Path $modDir "api"
  New-Item -ItemType Directory -Path $apiDir -Force | Out-Null

  $readme = Join-Path $modDir "README.md"
  $apiIndex = Join-Path $apiDir "index.md"

  $readmeContent = "# $m`n`nThis module does not have a README.md yet. If present in the module, the build will replace this with the actual README.`n"
  $apiContent = "# API Reference`n`nGenerated Dokka output not found for this module yet. After building with Maven (verify), Dokka-generated pages will appear here.`n"

  Set-Content -Path $readme -Value $readmeContent -Encoding UTF8
  Set-Content -Path $apiIndex -Value $apiContent -Encoding UTF8
}

Write-Host "Seeded placeholder Reference pages for modules: $($modules -join ', ')"