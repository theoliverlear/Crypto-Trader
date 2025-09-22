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
import {APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA, inject} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule} from "@angular/forms";
import {AppRouting} from "./routing/app-routing.module";
import {RouterOutlet} from "@angular/router";
import {CommonModule, NgOptimizedImage} from "@angular/common";
import {HttpClientModule, provideHttpClient, withFetch, withInterceptors} from "@angular/common/http";
import {services} from "../services/services";
import {BaseChartDirective} from "ng2-charts";
import { AngularSuiteModule } from "@theoliverlear/angular-suite";
import {AppComponent} from "../components/app/app.component";
import {directives} from "../directives/directives";
import {elements} from "../components/elements/elements";
import {pages} from "../components/pages/pages";
import {authInterceptor} from "../services/intercept/auth.interceptor";
import { AuthService } from "../services/net/http/auth/auth.service";
import { DpopKeyService } from "../services/auth/dpop/dpop-key.service";

/**
 * APP_INITIALIZER: ensure we have a DPoP key and try to refresh the access token
 * using the HttpOnly refresh cookie automatically on app start.
 */
export function appInit(): () => Promise<void> {
    return async () => {
        const keys = inject(DpopKeyService);
        const auth = inject(AuthService);
        try {
            await keys.ensureKeys();
            await new Promise<void>((resolve) => {
                auth.refresh().subscribe({ next: () => resolve(), error: () => resolve() });
            });
        } catch {
            // ignore startup refresh errors
        }
    };
}

@NgModule({
    declarations: [
        AppComponent,
        ...elements,
        ...pages,
    ],
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        AppRouting,
        RouterOutlet,
        HttpClientModule,
        BaseChartDirective,
        AngularSuiteModule
    ],
    providers: [
        ...services,
        provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
        { provide: APP_INITIALIZER, useFactory: appInit, multi: true },
    ],
    bootstrap: [AppComponent],
    exports: [],
    schemas: []
})
export class CryptoTraderModule {
    constructor() {
        
    }
}