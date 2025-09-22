import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";
import {PersistMethod, PossibleToken} from "../../models/auth/types";

/**
 * In-memory-only access token storage. No localStorage/sessionStorage usage.
 * Provides both imperative getter and reactive observable.
 */
@Injectable({ providedIn: 'root' })
export class TokenStorageService {
    private readonly token$ = new BehaviorSubject<PossibleToken>(null);

    getToken(): PossibleToken {
        return this.token$.value;
    }

    observe(): Observable<PossibleToken> {
        return this.token$.asObservable();
    }

    setToken(token: PossibleToken, _persist: PersistMethod = 'session'): void {
        // persist parameter kept for API compatibility but ignored to enforce in-memory policy
        this.token$.next(token);
    }

    clear(): void {
        this.token$.next(null);
    }
}
