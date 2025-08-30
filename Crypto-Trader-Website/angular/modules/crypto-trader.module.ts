import {NgModule} from "@angular/core";
import {AppComponent} from "../components/app/app.component";
import {elements} from "../components/elements/elements";
import {directives} from "../directives/directives";
import {pages} from "../components/pages/pages";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule} from "@angular/forms";
import {AppRouting} from "./routing/app-routing.module";
import {RouterOutlet} from "@angular/router";
import {NgOptimizedImage} from "@angular/common";
import {HttpClientModule, provideHttpClient, withFetch} from "@angular/common/http";
import {services} from "../services/services";
import {BaseChartDirective} from "ng2-charts";
import {AngularSuiteModule} from "@theoliverlear/angular-suite";

@NgModule({
    declarations: [
        AppComponent,
        ...elements,
        ...directives,
        ...pages,
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        AppRouting,
        RouterOutlet,
        NgOptimizedImage,
        HttpClientModule,
        BaseChartDirective,
        AngularSuiteModule
    ],
    providers: [
        ...services,
        provideHttpClient(withFetch()),
    ],
    bootstrap: [AppComponent],
    exports: [],
    schemas: []
})
export class CryptoTraderModule {
    constructor() {
        
    }
}