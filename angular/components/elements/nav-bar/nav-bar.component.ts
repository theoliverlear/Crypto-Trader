import {
  Component
} from '@angular/core';


@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar-style.component.css']
})
export class NavBarComponent {
  constructor() {
    console.log('NavBarComponent loaded');
  }
}
