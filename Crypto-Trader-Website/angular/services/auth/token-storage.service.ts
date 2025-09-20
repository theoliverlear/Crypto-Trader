import {Injectable} from "@angular/core";
import {PersistMethod, PossibleToken} from "../../models/auth/types";

@Injectable({ providedIn: 'root' })
export class TokenStorageService {
    private tokenInMemory: PossibleToken = null;
    private readonly storageKey = 'ct_jwt';

    getToken(): PossibleToken {
        if (this.tokenInMemory) return this.tokenInMemory;
        try {
            return sessionStorage.getItem(this.storageKey) || localStorage.getItem(this.storageKey);
        } catch {
            return null;
        }
    }

    setToken(token: PossibleToken, persist: PersistMethod = 'local'): void {
        this.tokenInMemory = token;
        try {
            sessionStorage.removeItem(this.storageKey);
            localStorage.removeItem(this.storageKey);
            if (token) {
                if (persist === 'local') {
                    localStorage.setItem(this.storageKey, token);
                } else if (persist === 'session') {
                    sessionStorage.setItem(this.storageKey, token);
                }
            }
        } catch {
            console.error("Key storage failure.")
        }
    }

    clear(): void {
        this.setToken(null);
    }
}
