// Generates Angular environment files at build time so they are not committed.
// Reads values from process.env with sensible defaults.
// This script is intentionally minimal and framework-agnostic.

import { mkdirSync, writeFileSync, existsSync } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

function boolFromEnv(val, fallback = false) {
    if (val === undefined) return fallback;
    const s = String(val).toLowerCase();
    return s === '1' || s === 'true' || s === 'yes' || s === 'on';
}

const ENV = {
    // Common
    API_URL: process.env.WEBSITE_API_URL || process.env.API_URL || 'http://localhost:3000/api',
    DATA_URL: process.env.WEBSITE_DATA_URL || process.env.DATA_URL || (process.env.WEBSITE_API_URL || process.env.API_URL || 'http://localhost:3000/api'),
    WEBSOCKET_URL: process.env.WEBSITE_WEBSOCKET_URL || process.env.WEBSOCKET_URL || 'ws://localhost:3000/ws',
    ENABLE_DEBUG: boolFromEnv(process.env.WEBSITE_ENABLE_DEBUG ?? process.env.ENABLE_DEBUG, true),
    FEATURE_FLAG: boolFromEnv(process.env.WEBSITE_FEATURE_FLAG ?? process.env.FEATURE_FLAG, false),
};

const outDir = resolve(__dirname, '..', 'angular', 'environments');
if (!existsSync(outDir)) {
    mkdirSync(outDir, { recursive: true });
}

function fileContent({ production, apiUrl, dataUrl, websocketUrl, enableDebug, featureFlag }) {
    // Produce a TypeScript module exporting the environment object.
    return `export const environment = {
  production: ${production},
  apiUrl: ${JSON.stringify(apiUrl)},
  dataUrl: ${JSON.stringify(dataUrl)},
  websocketUrl: ${JSON.stringify(websocketUrl)},
  enableDebug: ${enableDebug},
  featureFlag: ${featureFlag}
};
`;
}

const files = [
    {
        name: 'environment.ts',
        data: fileContent({
            production: false,
            apiUrl: ENV.API_URL,
            dataUrl: ENV.DATA_URL,
            websocketUrl: ENV.WEBSOCKET_URL,
            enableDebug: ENV.ENABLE_DEBUG,
            featureFlag: ENV.FEATURE_FLAG,
        }),
    },
    {
        name: 'environment.dev.ts',
        data: fileContent({
            production: false,
            apiUrl: ENV.API_URL,
            dataUrl: ENV.DATA_URL,
            websocketUrl: ENV.WEBSOCKET_URL,
            enableDebug: ENV.ENABLE_DEBUG,
            featureFlag: ENV.FEATURE_FLAG,
        }),
    },
    {
        name: 'environment.prod.ts',
        data: fileContent({
            production: true,
            apiUrl: process.env.WEBSITE_API_URL_PROD || process.env.API_URL_PROD || ENV.API_URL.replace('http://', 'https://').replace(':3000', ''),
            dataUrl: process.env.WEBSITE_DATA_URL_PROD || process.env.DATA_URL_PROD || (process.env.WEBSITE_API_URL_PROD || process.env.API_URL_PROD || ENV.DATA_URL).replace('http://', 'https://').replace(':3000', ''),
            websocketUrl: process.env.WEBSOCKET_URL_PROD || process.env.WEBSOCKET_URL_PROD || ENV.WEBSOCKET_URL.replace('http://', 'https://').replace(':3000', ''),
            enableDebug: boolFromEnv(process.env.WEBSITE_ENABLE_DEBUG_PROD ?? process.env.ENABLE_DEBUG_PROD, false),
            featureFlag: boolFromEnv(process.env.WEBSITE_FEATURE_FLAG_PROD ?? process.env.FEATURE_FLAG_PROD, true),
        }),
    },
];

for (const f of files) {
    const p = resolve(outDir, f.name);
    writeFileSync(p, f.data, 'utf8');
}

// Optional: emit a brief log for CI debugging
if (boolFromEnv(process.env.VERBOSE_ENV_GEN)) {
    console.log('[env-gen] Generated env files at', outDir);
}
