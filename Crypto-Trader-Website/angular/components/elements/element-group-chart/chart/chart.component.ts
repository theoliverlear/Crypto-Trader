// chart.component.ts 
import { Component, ElementRef, Input, OnChanges, SimpleChanges, ViewChild } from "@angular/core";
import * as d3 from 'd3';
import {
    CurrencyFormatterService
} from "../../../../services/ui/currency-formatter.service";

export type SparkPoint = { 
    date: Date | string | number;
    value: number;
};
export type Margin = {
    top: number;
    right: number;
    bottom: number;
    left: number;
}
@Component({
    selector: 'chart',
    standalone: false,
    templateUrl: './chart.component.html',
    styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnChanges {
    @Input() data: SparkPoint[] = [];
    @Input() width: number = 120;
    @Input() height: number = 40;
    @Input() stroke: string = '#4caf50';
    @Input() strokeWidth: number = 2.0;
    @Input() margin: Margin = {top: 20, right: 20, bottom: 20, left: 20};

    @ViewChild('svgElement', { static: true }) chartReference!: ElementRef<SVGSVGElement>;

    constructor(private currencyFormatter: CurrencyFormatterService) {

    }
    
    ngOnChanges(changes: SimpleChanges): void {
        if (changes['data']) {
            this.render();
        }
    }

    ngAfterViewInit(): void {
        this.render();
    }

    private render(): void {
        const svgElement: SVGSVGElement = this.chartReference?.nativeElement;
        if (!svgElement) {
            return;
        }

        const width: number = this.getAdjustedWidth();
        const height: number = this.getAdjustedHeight();
        this.resetSVG(svgElement);
        this.setDimensions(svgElement);

        const graphic: any = d3.select(svgElement)
            .append('g')
            .attr('transform', `translate(${this.margin.left},${this.margin.top})`);

        if (!this.data || this.data.length === 0) {
            return;
        }

        const parsed: {date: Date; value: number}[] = this.data.map(point => ({
            date: point.date instanceof Date ? point.date : new Date(point.date),
            value: point.value
        })).filter(date => !isNaN((date.date as Date).getTime()));
        if (parsed.length === 0) {
            return;
        }

        const xAxis: any = d3.scaleTime()
            .domain(d3.extent(parsed, (d: {date: Date; value: number}) => d.date) as [Date, Date])
            .range([0, width]);
        const yAxis: any = d3.scaleLinear()
            .domain(d3.extent(parsed, (d: {date: Date; value: number}) => d.value) as [number, number])
            .nice()
            .range([height, 0]);

        const line = d3.line()
            .x((d: any) => xAxis((d as any).date))
            .y((d: any) => yAxis((d as any).value))
            .defined((d: any) => Number.isFinite((d as any).value));

        graphic.append('path')
            .datum(parsed as any)
            .attr('fill', 'none')
            .attr('stroke', this.stroke)
            .attr('stroke-width', this.strokeWidth)
            .attr('d', line as any);

        const [yMin, yMax] = yAxis.domain();
        const labelPadTopEm: number = 1.4;
        const labelPadBottomEm: number = -0.5;

        graphic.append('text')
            .attr('x', width)
            .attr('y', yAxis(yMax))
            .attr('dx', '-6')
            .attr('dy', `${labelPadTopEm}em`)
            .attr('text-anchor', 'end')
            .attr('fill', '#9aa0a6')
            .attr('font-size', 10)
            .text(this.currencyFormatter.formatCurrency(yMax));

        graphic.append('text')
            .attr('x', width)
            .attr('y', yAxis(yMin))
            .attr('dx', '-6')
            .attr('dy', `-${labelPadBottomEm}em`)
            .attr('text-anchor', 'end')
            .attr('fill', '#9aa0a6')
            .attr('font-size', 10)
            .text(this.currencyFormatter.formatCurrency(yMin));
        
    }

    private setDimensions(svgElement: SVGSVGElement) {
        svgElement.setAttribute('width', String(this.width));
        svgElement.setAttribute('height', String(this.height));
    }

    private resetSVG(svgElement: SVGSVGElement) {
        while (svgElement.firstChild) {
            svgElement.removeChild(svgElement.firstChild);
        }
    }

    private getAdjustedHeight() {
        return this.height - this.margin.top - this.margin.bottom;
    }

    private getAdjustedWidth() {
        return this.width - this.margin.left - this.margin.right;
    }
}
