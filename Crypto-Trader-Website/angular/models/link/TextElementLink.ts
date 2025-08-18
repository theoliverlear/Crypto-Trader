import {ElementLink} from "./ElementLink";
import {TagType} from "../html/TagType";
import {TargetType} from "../html/TargetType";

export class TextElementLink extends ElementLink {
    text: string;
    tagType: TagType;
    constructor(hrefLink: string = '',
                targetType: TargetType = TargetType.SELF,
                useHyperlinkStyle: boolean = false,
                text: string = '',
                tagType: TagType = TagType.P) {
        super(hrefLink, targetType, useHyperlinkStyle);
        this.text = text;
        this.tagType = tagType;
    }
    hasText(): boolean {
        console.log('TextElementLink.hasText()');
        return this.text.length > 0;
    }
}