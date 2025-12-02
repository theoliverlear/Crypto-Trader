import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter, map, mergeMap} from "rxjs";
import {AuthGuard} from "../../services/guard/auth.guard";
import {DelayService} from "@theoliverlear/angular-suite";
import {
    NavBarComponent
} from "../elements/element-group-nav/nav-bar/nav-bar.component";

@Component({
    selector: 'app',
    standalone: false,
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    showAuthGuardPopup: boolean = false;
    title: string;
    @ViewChild(NavBarComponent) navBar: NavBarComponent;
    
    constructor(private router: Router, 
                private activatedRoot: ActivatedRoute,
                private authGuard: AuthGuard,
                private delayService: DelayService) {
        
    }
    
    updateNavBar(): void {
        this.navBar.verifyLoginStatus();
    }
    
    ngOnInit(): void {
        this.router.events.pipe(filter((event) => event instanceof NavigationEnd),
            map(() => this.activatedRoot),
            map((route) => {
                while (route.firstChild) {
                    route = route.firstChild;
                }
                return route;
            }),
            mergeMap((route) => route.data)
        ).subscribe((data) => {
            const metaInfo = data['meta'] || {};
            this.title = metaInfo['title'] || 'Crypto Trader';
            this.updateNavBar();
        });
        this.authGuard.getAuthBlocked().subscribe((authBlocked: undefined) => {
            this.triggerAuthPopup();
        })
    }
    
    async triggerAuthPopup(): Promise<void> {
        this.showAuthGuardPopup = true;
        await this.delayService.delay(4200);
        this.showAuthGuardPopup = false;
    }
}