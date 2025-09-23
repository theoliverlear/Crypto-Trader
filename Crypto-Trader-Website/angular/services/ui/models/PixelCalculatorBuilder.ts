import {PixelVariables} from "./types";
import {PixelCalculator} from "./PixelCalculator";

export class PixelCalculatorBuilder {
    private _vw = 0;
    private _vh = 0;
    private _rem = 0;
    
    constructor(private pixelCalculator: PixelCalculator) { }

    public vw(vw: number): PixelCalculatorBuilder {
        this._vw = vw;
        return this;
    }
    
    public vh(vh: number): PixelCalculatorBuilder {
        this._vh = vh;
        return this;
    }
    
    public rem(rem: number): PixelCalculatorBuilder {
        this._rem = rem;
        return this;
    }

    public pixels(): number {
        const vwPixels: number = this.pixelCalculator.vwToPixels(this._vw);
        const vhPixels: number = this.pixelCalculator.vhToPixels(this._vh);
        const remPixels: number = this.pixelCalculator.remToPixels(this._rem);
        return this.pixelCalculator.getByViewportRem(vwPixels, vhPixels, remPixels);
    }
    
    public build(): PixelVariables {
        return {
            vw: this._vw,
            vh: this._vh,
            rem: this._rem,
        };
    }
}