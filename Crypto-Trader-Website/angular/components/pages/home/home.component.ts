import { Component } from '@angular/core';
import {TagType} from "../../../models/html/TagType";
import {transparentLogo} from "../../../assets/imageAssets";
import {cryptoTraderDescription} from "../../../assets/textAssets";
import {ElementSize} from "../../../models/ElementSize";
import {
    ButtonText
} from "../../elements/element-group-native/ss-button/models/ButtonText";
import {getStartedElementLink} from "../../../assets/elementLinkAssets";

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

    protected readonly TagType = TagType;
    protected readonly transparentLogo = transparentLogo;
    protected readonly cryptoTraderDescription = cryptoTraderDescription;
    protected readonly crypto = crypto;
    protected readonly ElementSize = ElementSize;
    protected readonly ButtonText = ButtonText;
    protected readonly getStartedElementLink = getStartedElementLink;
}
