/**
 * Angular root module that wires up:
 * - APP_INITIALIZER (appInit): ensures a persistent DPoP key exists and performs a silent
 *   POST /auth/refresh on startup with withCredentials so the HttpOnly refresh cookie can
 *   restore an in‑memory access token after reloads.
 * - provideHttpClient + authInterceptor: attaches DPoP proofs and DPoP Authorization headers
 *   to protected API requests, and restricts withCredentials to refresh/logout only.
 *
 * This module is intentionally minimal: components/services imported here contain the bulk of
 * UI and networking logic. Changing the bootstrap order or removing APP_INITIALIZER would
 * reintroduce the “signed out after reload” symptom because access tokens are in‑memory only.
 */
import { CommonModule } from '@angular/common';
import {
    HttpClientModule,
    provideHttpClient,
    withFetch,
    withInterceptors,
} from '@angular/common/http';
import { APP_INITIALIZER, inject, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatOption,
} from '@angular/material/autocomplete';
import { MatIconButton } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatFormField, MatInput, MatSuffix } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterOutlet } from '@angular/router';
import { BaseChartDirective } from 'ng2-charts';

import { AngularSuiteModule } from '@theoliverlear/angular-suite';
import { authInterceptor } from '@services/intercept/auth.interceptor';
import { services } from '@services/services';
import { AppComponent } from '@components/app/app.component';
import { elements } from '@components/elements/elements';
import { pages } from '@components/pages/pages';
import { AuthService } from '@http/auth/auth.service';
import { DpopKeyService } from '@auth/dpop/dpop-key.service';

import { AppRouting } from './routing/app-routing.module';

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
    declarations: [AppComponent, ...elements, ...pages],
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        AppRouting,
        RouterOutlet,
        HttpClientModule,
        BaseChartDirective,
        AngularSuiteModule,
        MatFormField,
        MatInput,
        MatSelectModule,
        MatSlideToggleModule,
        MatExpansionModule,
        MatProgressSpinnerModule,
        ReactiveFormsModule,
        MatAutocompleteTrigger,
        MatOption,
        MatAutocomplete,
        MatIconModule,
        MatIconButton,
        MatSuffix,
    ],
    providers: [
        ...services,
        provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
        { provide: APP_INITIALIZER, useFactory: appInit, multi: true },
    ],
    bootstrap: [AppComponent],
    exports: [],
    schemas: [],
})
export class CryptoTraderModule {
    constructor() {}
}
