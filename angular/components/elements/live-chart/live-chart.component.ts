import {Component, ElementRef, Input, OnInit} from "@angular/core";
import {ChartType} from "./models/ChartType";

@Component({
    selector: 'live-chart',
    templateUrl: './live-chart.component.html',
    styleUrls: ['./live-chart-style.component.css']
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