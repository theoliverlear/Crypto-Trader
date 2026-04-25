import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AngularSuiteModule } from '@theoliverlear/angular-suite';

import { UniversalModule } from '../shared/universal.module';
import { ChartModule } from '../shared/chart.module';
import { DayCurrencyChartComponent } from '@components/elements/element-group-currency/day-currency-chart/day-currency-chart.component';
import { DisplayCurrencyComponent } from '@components/elements/element-group-currency/display-currency/display-currency.component';
import { PerformanceArrowComponent } from '@components/elements/element-group-currency/performance-arrow/performance-arrow.component';
import { CurrenciesComponent } from '@components/pages/currencies/currencies.component';

const currencyComponents = [
    DayCurrencyChartComponent,
    DisplayCurrencyComponent,
    PerformanceArrowComponent,
    CurrenciesComponent,
];

@NgModule({
    declarations: [...currencyComponents],
    imports: [
        CommonModule,
        FormsModule,
        AngularSuiteModule,
        UniversalModule,
        ChartModule,
    ],
    exports: [...currencyComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class CurrencyModule {}
