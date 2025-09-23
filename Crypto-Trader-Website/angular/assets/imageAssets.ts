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

export const upArrowIcon: ImageAsset = {
    src: getIconImagePath('up_arrow_icon.svg'),
    alt: 'Up Arrow Icon'
};

export const consoleIcon: ImageAsset = {
    src: getIconImagePath('console_icon.svg'),
    alt: 'Console Icon'
};

export const profileIcon: ImageAsset = {
    src: getIconImagePath('profile_icon.svg'),
    alt: 'Profile Icon'
};

export const exitIcon: ImageAsset = {
    src: getIconImagePath('white_exit_door_icon.svg'),
    alt: 'Exit Icon'
};

export const walletIcon: ImageAsset = {
    src: getIconImagePath('wallet_icon.svg'),
    alt: 'Wallet Icon'
};

export const coinIcon: ImageAsset = {
    src: getIconImagePath('coin_icon.svg'),
    alt: 'Coin Icon'
};

export const stockScaleIcon: ImageAsset = {
    src: getIconImagePath('stock_scale_icon.svg'),
    alt: 'Stock Scale Icon'
};

export const exchangeArrowsIcon: ImageAsset = {
    src: getIconImagePath('exchange_arrows_icon.svg'),
    alt: 'Soft Exchange Arrows Icon'
};

export const paperIcon: ImageAsset = {
    src: getIconImagePath('paper_icon.svg'),
    alt: 'Paper Icon'
};

export const starIcon: ImageAsset = {
    src: getIconImagePath('star_icon.png'),
    alt: 'Star Icon'
};