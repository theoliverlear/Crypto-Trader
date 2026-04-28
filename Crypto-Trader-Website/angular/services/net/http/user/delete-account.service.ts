// delete-account.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '@environments/environment';
import { OperationSuccessResponse } from '@models/types';

/** HTTP service that sends a delete request to remove the authenticated user's account.
 *
 */
@Injectable({
    providedIn: 'root',
})
export class DeleteAccountService {
    private static readonly URL: string = `${environment.apiUrl}/account/delete`;

    constructor(private readonly http: HttpClient) {}

    /** Delete the authenticated user's account and all associated data.
     * @returns An observable that emits the operation result.
     */
    public deleteAccount(): Observable<OperationSuccessResponse> {
        return this.http.delete<OperationSuccessResponse>(DeleteAccountService.URL);
    }
}
