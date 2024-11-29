export interface ImageAsset {
    src: string;
    alt: string;
}

export function getImagePath(fileName: string): string {
    return imageAssetPath + fileName;
}
export function getCryptoTraderLogo(fileName: string): string {
    return logoImageAssetPath + fileName;
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
const logoImageAssetPath: string = imageAssetPath + 'logo/';
const cryptoTraderLogoImageAssetPath: string = logoImageAssetPath + 'crypto_trader/';
const brandLogoImageAssetPath: string = logoImageAssetPath + 'brand/';
const currencyLogoImageAssetPath: string = logoImageAssetPath + 'currency/';
const iconImageAssetPath: string = imageAssetPath + 'icon/';

export const defaultAvatar: ImageAsset = {
    src: getIconImagePath('default-avatar.png'),
    alt: 'Default Avatar'
};