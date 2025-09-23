import {
    coinIcon,
    ImageAsset, exchangeArrowsIcon, stockScaleIcon,
    walletIcon
} from "../../../../../assets/imageAssets";
import {ElementLink} from "@theoliverlear/angular-suite";
import {
    currenciesElementLink,
    portfolioElementLink, tradeElementLink, traderElementLink
} from "../../../../../assets/elementLinkAssets";

export enum NavBarItemOption {
    CURRENCIES = "Currencies",
    PORTFOLIO = "Portfolio",
    TRADER = "Trader",
    TRADE = "Trade"
}
export namespace NavBarItemOption {
    export function getImageAsset(option: NavBarItemOption): ImageAsset {
        switch (option) {
            case NavBarItemOption.CURRENCIES:
                return coinIcon;
            case NavBarItemOption.PORTFOLIO:
                return walletIcon;
            case NavBarItemOption.TRADER:
                return stockScaleIcon;
            case NavBarItemOption.TRADE:
                return exchangeArrowsIcon;
            default:
                throw new Error("Invalid option: " + option);
        }
    }
    
    export function getElementLink(option: NavBarItemOption): ElementLink {
        switch (option) {
            case NavBarItemOption.CURRENCIES:
                return currenciesElementLink;
            case NavBarItemOption.PORTFOLIO:
                return portfolioElementLink;
            case NavBarItemOption.TRADER:
                return traderElementLink;
            case NavBarItemOption.TRADE:
                return tradeElementLink;
            default:
                throw new Error("Invalid option: " + option);
        }
    }
    
    export function values(): NavBarItemOption[] {
        return [
            NavBarItemOption.CURRENCIES,
            NavBarItemOption.PORTFOLIO,
            NavBarItemOption.TRADER,
            NavBarItemOption.TRADE
        ];
    }
}