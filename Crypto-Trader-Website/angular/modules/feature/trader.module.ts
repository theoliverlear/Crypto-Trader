import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { AngularSuiteModule } from '@theoliverlear/angular-suite';

import { UniversalModule } from '../shared/universal.module';
import { TraderEventBubbleComponent } from '@components/elements/element-group-trader/trader-event-bubble/trader-event-bubble.component';
import { TraderEventFilterBarComponent } from '@components/elements/element-group-trader/trader-event-filter-bar/trader-event-filter-bar.component';
import { TraderReportComponent } from '@components/elements/element-group-trader/element-group-trader-report/trader-report/trader-report.component';
import { TraderReportRangeSelectorComponent } from '@components/elements/element-group-trader/element-group-trader-report/trader-report-range-selector/trader-report-range-selector.component';
import { TraderComponent } from '@components/pages/trader/trader.component';

const traderComponents = [
    TraderEventBubbleComponent,
    TraderEventFilterBarComponent,
    TraderReportComponent,
    TraderReportRangeSelectorComponent,
    TraderComponent,
];

@NgModule({
    declarations: [...traderComponents],
    imports: [
        CommonModule,
        AngularSuiteModule,
        UniversalModule,
    ],
    exports: [...traderComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class TraderModule {}
