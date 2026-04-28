import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '@environments/environment';
import { OperationSuccessResponse } from '@models/types';

/** HTTP service that sends a DELETE request to remove the authenticated user's account
 * and all associated data.
 */
@Injectable({
    providedIn: 'root',
})
export class DeleteAccountService {
    private static readonly URL: string = `${environment.apiUrl}/user/me`;

    constructor(private readonly http: HttpClient) {}

    /** Delete the current user's account and all associated data.
     * @returns Observable that completes when the account has been deleted.
     */
    public deleteAccount(): Observable<OperationSuccessResponse> {
        return this.http.delete<OperationSuccessResponse>(
            DeleteAccountService.URL,
        );
    }
}
