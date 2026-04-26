// module-architecture-map.component.ts
import { Component, Input } from '@angular/core';

export interface ArchitectureLayer {
    label: string;
    nodes: { name: string; highlight?: boolean; small?: boolean }[];
}

@Component({
    selector: 'module-architecture-map',
    standalone: false,
    templateUrl: './module-architecture-map.component.html',
    styleUrls: ['./module-architecture-map.component.scss'],
})
export class ModuleArchitectureMapComponent {
    @Input() public layers: ArchitectureLayer[] = [];
    @Input() public allModuleNames: string[] = [];
}
