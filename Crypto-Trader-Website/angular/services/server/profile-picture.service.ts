import {Injectable} from "@angular/core";
import {catchError, map, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ErrorHandlerService} from "../error-handler.service";
import {httpOptions} from "./httpProperties";

@Injectable({
    providedIn: 'root'
})
export class ProfilePictureService {

    constructor(private http: HttpClient,
                private errorHandlerService: ErrorHandlerService) {
        console.log('ProfilePictureService created');
    }
    getHasProfilePictureFromServer(userId: number): Observable<boolean> {
        const serverUrl = `/profile/get/${userId}/has-profile-picture`;
        return this.http.post<{ hasProfilePicture: boolean }>(serverUrl, httpOptions).pipe(
            map((response: {hasProfilePicture: boolean}) => response.hasProfilePicture),
            catchError(this.errorHandlerService.handleError<boolean>('getHasProfilePictureFromServer', false))
        )
    }
}