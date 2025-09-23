import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter, map, mergeMap} from "rxjs";
import {AuthGuard} from "../../services/guard/auth.guard";
import {DelayService} from "@theoliverlear/angular-suite";

@Component({
    selector: 'app',
    standalone: false,
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    showAuthGuardPopup: boolean = false;
    title: string;
    constructor(private router: Router, 
                private activatedRoot: ActivatedRoute,
                private authGuard: AuthGuard,
                private delayService: DelayService) {
        
    }
    ngOnInit() {
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
        });
        this.authGuard.getAuthBlocked().subscribe((authBlocked: undefined) => {
            this.triggerAuthPopup();
        })
    }
    
    async triggerAuthPopup(): Promise<void> {
        this.showAuthGuardPopup = true;
        // Keep visible slightly longer than the animation duration for a smooth finish
        await this.delayService.delay(4200);
        this.showAuthGuardPopup = false;
    }
}