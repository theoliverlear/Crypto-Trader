import {HttpHeaders} from "@angular/common/http";

export const httpOptions: { headers: HttpHeaders } = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json'
    })
};