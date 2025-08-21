import {TextElementLink} from "../models/link/TextElementLink";
import {TargetType} from "../models/html/TargetType";
import {TagType} from "../models/html/TagType";
import {ElementLink} from "../models/link/ElementLink";

export const navBarHomeLink = new TextElementLink('',
    TargetType.SELF,
    false,
    'Home',
    TagType.H4
);
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