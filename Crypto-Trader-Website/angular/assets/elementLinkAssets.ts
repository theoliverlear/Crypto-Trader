import {
    TagType,
    TargetType,
    TextElementLink,
    ElementLink,
} from "@theoliverlear/angular-suite";

export const homeElementLink = new ElementLink('/',
    TargetType.SELF,
    false
);

export const navBarHomeLink = new TextElementLink('',
    TargetType.SELF,
    false,
    'Home',
    TagType.H4
);

export const navBarCurrenciesLink = new TextElementLink('/currencies',
    TargetType.SELF,
    false,
    'Currencies',
    TagType.H4,
)

export const navBarPortfolioLink = new TextElementLink('/portfolio',
    TargetType.SELF,
    false,
    'Portfolio',
    TagType.H4
);
export const navBarTraderLink = new TextElementLink('/trader',
    TargetType.SELF,
    false,
    'Trader',
    TagType.H4
);
export const navBarAccountLink = new ElementLink('/account',
    TargetType.SELF,
    false
);

export const getStartedElementLink = new TextElementLink('/authorize',
    TargetType.SELF,
    false,
    'Get Started',
    TagType.H4,
)

export const repositoryElementLink = new TextElementLink('https://github.com/theoliverlear/Crypto-Trader',
    TargetType.BLANK,
    false,
    'Explore Source Code',
    TagType.H4)