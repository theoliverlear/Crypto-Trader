import {CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule} from "@angular/forms";
import {AppRouting} from "./routing/app-routing.module";
import {RouterOutlet} from "@angular/router";
import {CommonModule, NgOptimizedImage} from "@angular/common";
import {HttpClientModule, provideHttpClient, withFetch, withInterceptors} from "@angular/common/http";
import {services} from "../services/services";
import {BaseChartDirective} from "ng2-charts";
import {
    AngularSuiteModule
} from "@theoliverlear/angular-suite";
import {AppComponent} from "../components/app/app.component";
import {directives} from "../directives/directives";
import {elements} from "../components/elements/elements";
import {pages} from "../components/pages/pages";
import {authInterceptor} from "../services/intercept/auth.interceptor";

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
    ],
    bootstrap: [AppComponent],
    exports: [],
    schemas: []
})
export class CryptoTraderModule {
    constructor() {
        
    }
}