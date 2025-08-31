// Entry point for NPM package consumers (no side effects)
// Provides a programmatic bootstrap for the Crypto Trader Angular app

import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { CryptoTraderModule } from './angular/modules/crypto-trader.module.js';

/**
 * Bootstraps the Crypto Trader Angular application programmatically.
 * Call this from your host page or app to start the UI.
 * @returns {Promise<import('@angular/core').NgModuleRef<any>>}
 */
export async function bootstrapCryptoTraderApp() {
  return platformBrowserDynamic().bootstrapModule(CryptoTraderModule);
}

// Also export the root Angular module for advanced integrations
export { CryptoTraderModule };
