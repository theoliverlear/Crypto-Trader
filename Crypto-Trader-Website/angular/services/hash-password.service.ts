import {Injectable} from "@angular/core";
import * as CryptoJS from 'crypto-js';

@Injectable({
    providedIn: 'root'
})
export class HashPasswordService {
    constructor() {
        
    }
    hashPassword(unhashedPassword: string): string {
        let hashedPassword: string = CryptoJS.SHA256(unhashedPassword).toString();
        return hashedPassword;
    }
}