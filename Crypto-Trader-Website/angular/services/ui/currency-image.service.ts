import {Injectable} from "@angular/core";
import {defaultCurrencyIcon, ImageAsset} from "../../assets/imageAssets";
import {DisplayCurrency} from "../../models/currency/types";

@Injectable({
    providedIn: 'root'
})
export class CurrencyImageService {
    constructor() {

    }

    async resolveImageAsset(currency: DisplayCurrency): Promise<ImageAsset> {
        let imageAsset: ImageAsset = {
            src: currency.logoUrl,
            alt: currency.currencyName + " logo",
        };
        const imageLoads: boolean = await this.imageLoads(imageAsset.src);
        if (!imageLoads) {
            imageAsset = defaultCurrencyIcon;
        }
        return imageAsset;
    }

    async imageLoads(src: string): Promise<boolean> {
        return new Promise<boolean>((resolve) => {
            const image = new Image();
            image.onload = () => {
                resolve(true);
            }
            image.onerror = () => {
                resolve(false);
            }
            image.src = src;
        });
    }
}