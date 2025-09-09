import {Component, ElementRef, Input, OnInit} from "@angular/core";
import {ChartType} from "./models/ChartType";

@Component({
    selector: 'live-chart',
    standalone: false,
    templateUrl: './live-chart.component.html',
    styleUrls: ['./live-chart.component.scss']
})
export class LiveChartComponent implements OnInit {
    chart: any;
    @Input() chartType: ChartType;
    constructor(private element: ElementRef) {

    }
    ngOnInit() {
        const canvas = this.element.nativeElement.querySelector('canvas').getContext('2d');
    }
}