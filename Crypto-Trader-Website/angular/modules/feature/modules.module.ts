import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { AngularSuiteModule } from '@theoliverlear/angular-suite';

import { UniversalModule } from '../shared/universal.module';
import { ModuleArchitectureMapComponent } from '@components/elements/element-group-modules/module-architecture-map/module-architecture-map.component';
import { ModuleCardComponent } from '@components/elements/element-group-modules/module-card/module-card.component';
import { ModuleInfraTileComponent } from '@components/elements/element-group-modules/module-infra-tile/module-infra-tile.component';
import { ModuleInterfaceSelectorComponent } from '@components/elements/element-group-modules/module-interface-selector/module-interface-selector.component';
import { ModulesComponent } from '@components/pages/modules/modules.component';

const modulesComponents = [
    ModuleArchitectureMapComponent,
    ModuleCardComponent,
    ModuleInfraTileComponent,
    ModuleInterfaceSelectorComponent,
    ModulesComponent,
];

@NgModule({
    declarations: [...modulesComponents],
    imports: [
        CommonModule,
        AngularSuiteModule,
        UniversalModule,
    ],
    exports: [...modulesComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ModulesModule {}
