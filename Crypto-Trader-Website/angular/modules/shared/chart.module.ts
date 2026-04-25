import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { AngularSuiteModule } from '@theoliverlear/angular-suite';
import { BaseChartDirective } from 'ng2-charts';

import { ChartComponent } from '@components/elements/element-group-chart/chart/chart.component';
import { LiveChartComponent } from '@components/elements/element-group-chart/live-chart/live-chart.component';

const chartComponents = [
    ChartComponent,
    LiveChartComponent,
];

@NgModule({
    declarations: [...chartComponents],
    imports: [
        CommonModule,
        BaseChartDirective,
        AngularSuiteModule,
    ],
    exports: [...chartComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ChartModule {}
