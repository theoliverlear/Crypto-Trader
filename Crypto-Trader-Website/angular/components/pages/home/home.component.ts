import { Component } from '@angular/core';
import {transparentLogo} from "../../../assets/imageAssets";
import {cryptoTraderDescription} from "../../../assets/textAssets";
import {getStartedElementLink} from "../../../assets/elementLinkAssets";
import {TagType, ElementSize} from "@theoliverlear/angular-suite";

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
    
    constructor() {
        
    }
    
    protected readonly TagType = TagType;
    protected readonly transparentLogo = transparentLogo;
    protected readonly cryptoTraderDescription = cryptoTraderDescription;
    protected readonly crypto = crypto;
    protected readonly ElementSize = ElementSize;
    protected readonly getStartedElementLink = getStartedElementLink;
}
