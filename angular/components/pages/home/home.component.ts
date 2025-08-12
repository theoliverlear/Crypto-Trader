import { Component } from '@angular/core';
import {TagType} from "../../../models/html/TagType";
import {transparentLogo} from "../../../assets/imageAssets";

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

    protected readonly TagType = TagType;
    protected readonly transparentLogo = transparentLogo;
}
