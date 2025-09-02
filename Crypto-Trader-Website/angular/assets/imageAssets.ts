export interface ImageAsset {
    src: string;
    alt: string;
}

export function getImagePath(fileName: string): string {
    return imageAssetPath + fileName;
}
export function getCryptoTraderLogo(fileName: string): string {
    return cryptoTraderLogoImageAssetPath + fileName;
}
export function getIconImagePath(fileName: string): string {
    return iconImageAssetPath + fileName;
}
export function getCryptoTraderLogoPath(fileName: string): string {
    return cryptoTraderLogoImageAssetPath + fileName;
}
export function getBrandLogoPath(fileName: string): string {
    return brandLogoImageAssetPath + fileName;
}
export function getCurrencyLogoPath(fileName: string): string {
    return currencyLogoImageAssetPath + fileName;
}

const imageAssetPath: string = 'assets/images/';
const logoImageAssetPath: string = imageAssetPath + 'logos/';
const cryptoTraderLogoImageAssetPath: string = logoImageAssetPath + 'crypto_trader/';
const brandLogoImageAssetPath: string = logoImageAssetPath + 'brand/';
const currencyLogoImageAssetPath: string = logoImageAssetPath + 'currency/';
const iconImageAssetPath: string = imageAssetPath + 'icons/';

export const defaultAvatar: ImageAsset = {
    src: getIconImagePath('default_avatar.png'),
    alt: 'Default Avatar'
};

export const transparentLogo: ImageAsset = {
    src: getCryptoTraderLogo('crypto_trader_logo_cropped_transparent.png'),
    alt: 'Transparent Logo'
}

export const stockIcon: ImageAsset = {
    src: getIconImagePath('stock_icon.svg'),
    alt: 'Stock Icon'
};

export const brainIcon: ImageAsset = {
    src: getIconImagePath('brain_icon.svg'),
    alt: 'Brain Icon'
};

export const aiStarIcon: ImageAsset = {
    src: getIconImagePath('ai_star_icon.svg'),
    alt: 'AI Star Icon'
};

export const defaultCurrencyIcon: ImageAsset = {
    src: getIconImagePath('default_currency_icon.png'),
    alt: 'Default Currency Icon'
};