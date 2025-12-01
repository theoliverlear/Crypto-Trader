import {
    coinIcon,
    ImageAsset, exchangeArrowsIcon, stockScaleIcon,
    walletIcon, circleCheckmarkIcon
} from "../../../../../assets/imageAssets";
import {ElementLink} from "@theoliverlear/angular-suite";
import {
    currenciesElementLink,
    portfolioElementLink,
    statisticsElementLink, tradeElementLink, traderElementLink
} from "../../../../../assets/elementLinkAssets";

export enum NavBarItemOption {
    CURRENCIES = "Currencies",
    PORTFOLIO = "Portfolio",
    TRADER = "Trader",
    TRADE = "Trade",
    STATISTICS = "Statistics"
}
export namespace NavBarItemOption {
    export function getImageAsset(option: NavBarItemOption): ImageAsset {
        switch (option) {
            case NavBarItemOption.CURRENCIES:
                return coinIcon;
            case NavBarItemOption.PORTFOLIO:
                return walletIcon;
            case NavBarItemOption.TRADER:
                return circleCheckmarkIcon;
            case NavBarItemOption.TRADE:
                return exchangeArrowsIcon;
            case NavBarItemOption.STATISTICS:
                return stockScaleIcon;
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
            case NavBarItemOption.STATISTICS:
                return statisticsElementLink;
            default:
                throw new Error("Invalid option: " + option);
        }
    }
    
    export function values(): NavBarItemOption[] {
        return [
            NavBarItemOption.CURRENCIES,
            NavBarItemOption.PORTFOLIO,
            NavBarItemOption.TRADER,
            NavBarItemOption.TRADE,
            NavBarItemOption.STATISTICS
        ];
    }
}