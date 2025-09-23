import {DOCUMENT, Inject, Injectable} from "@angular/core";
import {PixelCalculatorBuilder} from "./models/PixelCalculatorBuilder";
import {PixelCalculator} from "./models/PixelCalculator";

@Injectable({
    providedIn: 'root'
})
export class PixelCalculatorService implements PixelCalculator {
    private window: any;
    constructor(@Inject(DOCUMENT) private document: Document) {
        this.window = this.document.defaultView;
    }

    private get remPx(): number {
        const base = this.getByViewport(6, 6) + 12;
        return Math.min(base, 140);
    }

    public vwToPixels(vw: number): number {
        const screenWidth: number = this.window.innerWidth;
        return Math.floor(screenWidth * (vw / 100));
    }

    public vhToPixels(vh: number): number {
        const screenHeight: number = this.window.innerHeight;
        return Math.floor(screenHeight * (vh / 100));
    }
    
    public remToPixels(rem: number): number {
        return rem * this.remPx;
    }
    
    public getByViewport(vw: number, vh: number): number {
        return this.vwToPixels(vw) + this.vhToPixels(vh);
    }
    
    public getByViewportRem(vw: number, vh: number, rem: number): number {
        return this.vwToPixels(vw) + this.vhToPixels(vh) + this.remToPixels(rem);
    }

    public getByRem(rem: number): number {
        return this.remToPixels(rem);
    }

    public getByRemPx(rem: number, px: number): number {
        return this.remToPixels(rem) + px;
    }
    
    public builder(): PixelCalculatorBuilder {
        return new PixelCalculatorBuilder(this);
    }
}