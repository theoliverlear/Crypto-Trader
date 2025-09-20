// exit-anchor.component.ts
import {Component, HostListener} from "@angular/core";
import {exitIcon} from "../../../../assets/imageAssets";
import {homeElementLink} from "../../../../assets/elementLinkAssets";
import {LogoutService} from "../../../../services/net/http/logout.service";
import {
    TokenStorageService
} from "../../../../services/auth/token-storage.service";
import {AuthResponse} from "../../../../models/auth/types";
import {Router} from "@angular/router";

@Component({
    selector: 'exit-anchor',
    templateUrl: './exit-anchor.component.html',
    styleUrls: ['./exit-anchor.component.scss'],
    standalone: false
})
export class ExitAnchorComponent {
    constructor(private logoutService: LogoutService,
                private tokenStorageService: TokenStorageService,
                private router: Router) {
        
    }

    @HostListener('click')
    onClick() {
        try { 
            this.tokenStorageService.clear(); 
        } catch {
            console.error('Failed to clear token storage');
        }
        this.router.navigate(['/authorize']).then(navigated => {
            if (navigated) {
                this.logoutService.logout().subscribe({
                    next: (authResponse: AuthResponse) => {
                        console.log('Logged out: ', authResponse);
                    },
                    error: (error) => {
                        // Ignore errors; client state has been cleared
                        console.warn('Logout request error (ignored):', error);
                    },
                    complete: () => {
                        console.log('Logout complete');
                    }
                });
            }
        });

    }
    
    protected readonly exitIcon = exitIcon;
    protected readonly homeElementLink = homeElementLink;
}