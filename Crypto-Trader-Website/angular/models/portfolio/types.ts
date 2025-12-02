export type Portfolio = {
    dollarBalance: number;
    sharesBalance: number;
    totalWorth: number;
    lastUpdated: string;
    assets: PortfolioAsset[];
};

export type PortfolioAsset = {
    id: number,
    currencyName: string;
    currencyCode: string;
    shares: number;
    sharesValueInDollars: number;
    assetWalletDollars: number;
    totalValueInDollars: number;
    targetPrice: number;
    lastUpdated: string;
    vendorName: string;
};