// module-infra-tile.component.ts
import { Component, Input } from '@angular/core';
import { ModuleInfo } from '@models/module/ModuleInfo';

@Component({
    selector: 'module-infra-tile',
    standalone: false,
    templateUrl: './module-infra-tile.component.html',
    styleUrls: ['./module-infra-tile.component.scss'],
})
export class ModuleInfraTileComponent {
    // Remove the non-null assertion.
    @Input() public module!: ModuleInfo;
}
