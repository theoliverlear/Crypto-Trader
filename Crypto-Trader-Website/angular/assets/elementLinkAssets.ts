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

export const currenciesElementLink = new ElementLink('/currencies',
    TargetType.SELF,
    false
);

export const traderElementLink = new ElementLink('/trader',
    TargetType.SELF,
    false
);

export const portfolioElementLink = new ElementLink('/portfolio',
    TargetType.SELF,
    false
);

export const tradeElementLink = new ElementLink('/trade',
    TargetType.SELF,
    false
)

export const navBarCurrenciesTextLink = new TextElementLink('/currencies',
    TargetType.SELF,
    false,
    'Currencies',
    TagType.H4,
)

export const navBarPortfolioTextLink = new TextElementLink('/portfolio',
    TargetType.SELF,
    false,
    'Portfolio',
    TagType.H4
);
export const navBarTraderTextLink = new TextElementLink('/trader',
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
    TagType.H4);

export const repositoryElementLink = new TextElementLink('https://github.com/theoliverlear/Crypto-Trader',
    TargetType.BLANK,
    false,
    'Explore Code',
    TagType.H4);

export const docsElementLink = new TextElementLink('https://theoliverlear.github.io/Crypto-Trader/',
    TargetType.BLANK,
    false,
    'Read Docs',
    TagType.H4);

export const consoleElementLink = new ElementLink('/console',
    TargetType.SELF,
    false);