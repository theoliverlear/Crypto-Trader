import {TargetType} from "../html/TargetType";

export class ElementLink {
    hrefLink: string;
    targetType: TargetType;
    useHyperlinkStyle: boolean;
    constructor(hrefLink: string = '',
                targetType: TargetType = TargetType.SELF,
                useHyperlinkStyle: boolean = false) {
        this.hrefLink = hrefLink;
        this.targetType = targetType
        this.useHyperlinkStyle = useHyperlinkStyle;
    }
}