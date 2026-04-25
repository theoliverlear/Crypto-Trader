import { APP_INITIALIZER, inject, NgModule, Optional, SkipSelf } from '@angular/core';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';

import { AngularSuiteModule, DelayService } from '@theoliverlear/angular-suite';
import { authInterceptor } from '@services/intercept/auth.interceptor';
import { services } from '@services/services';
import { AuthService } from '@http/auth/auth.service';
import { DpopKeyService } from '@auth/dpop/dpop-key.service';

/**
 * APP_INITIALIZER: ensure we have a DPoP key and try to refresh the access token
 * using the HttpOnly refresh cookie automatically on app start.
 */
export function appInit(): () => Promise<void> {
    return async (): Promise<void> => {
        const keys: DpopKeyService = inject(DpopKeyService);
        const auth: AuthService = inject(AuthService);
        try {
            await keys.ensureKeys();
            await new Promise<void>((resolve): void => {
                auth.refresh().subscribe({
                    next: (): void => resolve(),
                    error: (): void => resolve(),
                });
            });
        } catch {
            // ignore startup refresh errors
        }
    };
}

@NgModule({
    imports: [
        AngularSuiteModule,
    ],
    exports: [
        AngularSuiteModule,
    ],
    providers: [
        ...services,
        DelayService,
        provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
        { provide: APP_INITIALIZER, useFactory: appInit, multi: true },
    ],
})
export class CryptoTraderCoreModule {
    constructor(@Optional() @SkipSelf() parentModule: CryptoTraderCoreModule) {
        if (parentModule) {
            throw new Error('CryptoTraderCoreModule is already loaded. Import it only in CryptoTraderModule.');
        }
    }
}
