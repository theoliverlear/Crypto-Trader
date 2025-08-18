import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import {CryptoTraderModule} from "./modules/crypto-trader.module";

console.log('main.ts loaded');

platformBrowserDynamic().bootstrapModule(CryptoTraderModule)
    .catch(err => console.error(err));
