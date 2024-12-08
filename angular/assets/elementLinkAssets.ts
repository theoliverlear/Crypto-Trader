import {TextElementLink} from "../models/link/TextElementLink";
import {TargetType} from "../models/html/TargetType";
import {TagType} from "../models/html/TagType";
import {ElementLink} from "../models/link/ElementLink";

export const navBarHomeLink = new TextElementLink('',
    TargetType.SELF,
    false,
    'Home',
    TagType.H5
);
export const navBarPortfolioLink = new TextElementLink('/portfolio',
    TargetType.SELF,
    false,
    'Portfolio',
    TagType.H5
);
export const navBarTraderLink = new TextElementLink('/trader',
    TargetType.SELF,
    false,
    'Trader',
    TagType.H5
);
export const navBarAccountLink = new ElementLink('/account',
    TargetType.SELF,
    false
);