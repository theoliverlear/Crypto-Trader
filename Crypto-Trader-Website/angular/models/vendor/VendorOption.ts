import {ImageAsset, paperIcon} from "../../assets/imageAssets";

export enum VendorOption {
    PAPER_MODE = "Paper Mode"
    // TODO: Add Coinbase and Binance.
}
export namespace VendorOption {
    export function getImageAsset(option: VendorOption): ImageAsset {
        switch (option) {
            case VendorOption.PAPER_MODE:
                return paperIcon;
        }
    }
    export function values(): VendorOption[] {
        return [
            VendorOption.PAPER_MODE,
        ];
    }
}