import {
    binanceBrandLogo,
    coinbaseBrandLogo,
    ImageAsset,
    paperIcon
} from "../../assets/imageAssets";

export enum VendorOption {
    PAPER_MODE = "Paper Mode",
    COINBASE = "Coinbase",
    BINANCE = "Binance",
}
export namespace VendorOption {
    export function getImageAsset(option: VendorOption): ImageAsset {
        switch (option) {
            case VendorOption.PAPER_MODE:
                return paperIcon;
            case VendorOption.COINBASE:
                return coinbaseBrandLogo;
            case VendorOption.BINANCE:
                return binanceBrandLogo;
        }
    }
    export function values(): VendorOption[] {
        return [
            VendorOption.PAPER_MODE,
            VendorOption.COINBASE,
            VendorOption.BINANCE
        ];
    }
}