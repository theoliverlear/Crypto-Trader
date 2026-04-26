import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { AngularSuiteModule } from '@theoliverlear/angular-suite';

import { UniversalModule } from '../shared/universal.module';
import { PromoModule } from './promo.module';
import { ModulesComponent } from '@components/pages/modules/modules.component';

// TODO: Fix module-promo sharing issue.

const modulesComponents = [
    ModulesComponent,
];

@NgModule({
    declarations: [...modulesComponents],
    imports: [
        CommonModule,
        AngularSuiteModule,
        UniversalModule,
        PromoModule,
    ],
    exports: [...modulesComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ModulesModule {}
