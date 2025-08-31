import 'zone.js/testing';
import { getTestBed } from '@angular/core/testing';
import {
    BrowserDynamicTestingModule,
    platformBrowserDynamicTesting
} from '@angular/platform-browser-dynamic/testing';

// TODO: This generated code needs cleaning up. Works, but is not optimal.
// TODO: NPM package for testing would fit into Crypto-Trader-Testing.

getTestBed().initTestEnvironment(
    BrowserDynamicTestingModule,
    platformBrowserDynamicTesting()
);

(function loadSpecs() {
    const hasRequireContext =
        typeof require !== 'undefined' && typeof (require as any).context === 'function';

    // Helper to iterate keys and require each module
    const loadFrom = (ctx: any) => {
        ctx.keys().forEach((key: string) => ctx(key));
    };

    if (hasRequireContext) {
        const context = (require as any).context('./', true, /\.spec\.ts$/);
        loadFrom(context);
    } else if ((import.meta as any).webpackContext) {
        // webpackContext(base, options)
        const context = (import.meta as any).webpackContext('./', {
            recursive: true,
            regExp: /\.spec\.ts$/
        });
        loadFrom(context);
    } else {
        throw new Error('No compatible test context loader found (require.context/import.meta.webpackContext).');
    }
})();

// Lightweight per-spec logger
(function attachJasmineLogging() {
    const GREEN = '\u001B[32m';
    const RED = '\u001B[31m';
    const YELLOW = '\u001B[33m';
    const RESET = '\u001B[0m';

    jasmine.getEnv().addReporter({
        specDone(result) {
            const icon = result.status === 'passed' ? `${GREEN}✓${RESET}` :
                result.status === 'failed' ? `${RED}✗${RESET}` : `${YELLOW}•${RESET}`;
            console.log(`${icon} ${result.fullName}`);
            if (result.failedExpectations?.length) {
                for (const f of result.failedExpectations) {
                    console.error(`${RED}  - ${f.message}${RESET}`);
                    if (f.stack) {
                        console.error(f.stack);
                    }
                }
            }
        }
    });
})();
