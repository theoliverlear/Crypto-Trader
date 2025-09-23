// chart.component.ts 
import {
    Component, ElementRef,
    HostListener, Input, OnChanges, SimpleChanges, ViewChild
} from "@angular/core";
import * as d3 from 'd3';
import {
    CurrencyFormatterService
} from "../../../../services/ui/currency-formatter.service";
import {
    ChartDisplayProperties,
    Margin,
    SparkPoint
} from "../../../../models/chart/types";
import {defaultChartProperties} from "../../../../assets/chartAssets";
import {
    PixelCalculatorService
} from "../../../../services/ui/pixel-calculator.service";


@Component({
    selector: 'chart',
    standalone: false,
    templateUrl: './chart.component.html',
    styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnChanges {
    @Input() properties: ChartDisplayProperties = defaultChartProperties;


    @ViewChild('svgElement', { static: true }) chartReference!: ElementRef<SVGSVGElement>;

    constructor(private currencyFormatter: CurrencyFormatterService,
                private pixelCalculator: PixelCalculatorService) {

    }
    
    @HostListener('window:resize', ['$event'])
    onResize(event: Event) {
        this.render();
    }
    
    ngOnChanges(changes: SimpleChanges): void {
        if (changes['properties']) {
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
        this.properties.width = this.pixelCalculator.getByViewport(40, 0);
        this.properties.height = this.pixelCalculator.getByViewport(0, 19);
        const width: number = this.getAdjustedWidth();
        const height: number = this.getAdjustedHeight();
        this.resetSVG(svgElement);
        this.setDimensions(svgElement);

        const graphic: any = d3.select(svgElement)
            .append('g')
            .attr('transform', `translate(${this.properties.margin.left},${this.properties.margin.top})`);

        if (!this.properties.data || this.properties.data.length === 0) {
            return;
        }

        const parsed: {date: Date; value: number}[] = this.properties.data.map(point => ({
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
            .attr('stroke', this.properties.stroke)
            .attr('stroke-width', this.properties.strokeWidth)
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
            .attr('fill', this.properties.textColor)
            .attr('font-size', 10)
            .text(this.currencyFormatter.formatCurrency(yMax));

        graphic.append('text')
            .attr('x', width)
            .attr('y', yAxis(yMin))
            .attr('dx', '-6')
            .attr('dy', `-${labelPadBottomEm}em`)
            .attr('text-anchor', 'end')
            .attr('fill', this.properties.textColor)
            .attr('font-size', 10)
            .text(this.currencyFormatter.formatCurrency(yMin));
        
        // Add simple time labels (HH:mm) at start and end of the x-axis
        let [domainStart, domainEnd] = xAxis.domain() as [Date, Date];

        // Ensure chronological order (left = earlier, right = later)
        if (domainStart > domainEnd) {
            const tmp = domainStart;
            domainStart = domainEnd;
            domainEnd = tmp;
        }

        // Floor to nearest minute to avoid second-level jitter
        const startTime = new Date(Math.floor(domainStart.getTime() / 60000) * 60000);
        const endTime = new Date(Math.floor(domainEnd.getTime() / 60000) * 60000);

        const formatTime = d3.timeFormat('%m-%d-%y, %-I%p').bind(d3);

        // Left (start) time label
        graphic.append('text')
            .attr('x', 0)
            .attr('y', height)
            .attr('dx', '6')
            .attr('dy', '1.6em')
            .attr('text-anchor', 'start')
            .attr('fill', this.properties.textColor)
            .attr('font-size', 10)
            .text(formatTime(startTime));

        // Right (end) time label
        graphic.append('text')
            .attr('x', width)
            .attr('y', height)
            .attr('dx', '-6')
            .attr('dy', '1.6em')
            .attr('text-anchor', 'end')
            .attr('fill', this.properties.textColor)
            .attr('font-size', 10)
            .text(formatTime(endTime));

    }

    private setDimensions(svgElement: SVGSVGElement) {
        svgElement.setAttribute('width', String(this.properties.width));
        svgElement.setAttribute('height', String(this.properties.height));
    }

    private resetSVG(svgElement: SVGSVGElement) {
        while (svgElement.firstChild) {
            svgElement.removeChild(svgElement.firstChild);
        }
    }

    private getAdjustedHeight() {
        return this.properties.height - this.properties.margin.top - this.properties.margin.bottom;
    }

    private getAdjustedWidth() {
        return this.properties.width - this.properties.margin.left - this.properties.margin.right;
    }
}
