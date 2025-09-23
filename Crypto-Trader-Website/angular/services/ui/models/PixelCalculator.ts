export interface PixelCalculator {
    getByViewportRem(vw: number, vh: number, rem: number): number;
    vwToPixels(vw: number): number;
    vhToPixels(vh: number): number;
    remToPixels(rem: number): number;
}