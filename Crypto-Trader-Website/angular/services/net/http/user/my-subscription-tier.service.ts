import { Injectable } from '@angular/core';
import { HttpClientService } from '@theoliverlear/angular-suite';
import { SubscriptionTier } from '@models/user/types';
import { environment } from '@environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

/** HTTP service that fetches the current user's subscription tier.
 *
 */
@Injectable({
    providedIn: 'root',
})
export class MySubscriptionTierService extends HttpClientService<never, SubscriptionTier> {
    private static readonly URL: string = `${environment.apiUrl}/user/me/tier`;
    private readonly subscriptionTier$: BehaviorSubject<SubscriptionTier> =
        new BehaviorSubject<SubscriptionTier>('FREE');
    constructor() {
        super(MySubscriptionTierService.URL);
    }

    /** Fetch subscription tier from API.
     * @returns The stream for fetching the subscription tier response.
     */
    public getMySubscriptionTier(): Observable<SubscriptionTier> {
        return this.get().pipe(
            map((response: SubscriptionTier): SubscriptionTier => {
                this.subscriptionTier$.next(response);
                return response;
            }),
        );
    }

    /** Obtains the current subscription tier already stored.
     * @returns The stream for the current subscription tier state.
     */
    public getSubscriptionTierStream(): Observable<SubscriptionTier> {
        return this.subscriptionTier$.asObservable();
    }
}
