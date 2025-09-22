// day-currency-chart.component.ts
import {
    Component,
    Input
} from "@angular/core";
import {
    ChartDisplayProperties
} from "../../../../models/chart/types";
@Component({
    selector: 'day-currency-chart',
    templateUrl: './day-currency-chart.component.html',
    styleUrls: ['./day-currency-chart.component.scss'],
    standalone: false
})
export class DayCurrencyChartComponent{
    @Input() properties: ChartDisplayProperties;


    constructor() { }

}