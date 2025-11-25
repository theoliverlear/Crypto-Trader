import {PossibleToken} from "../../../models/auth/types";
import {
    TokenStorageService
} from "../../../services/auth/token-storage.service";
import {Subscription} from "rxjs";

describe('TokenStorageService', () => {
    let tokenStorageService: TokenStorageService;
    beforeEach(() => {
        tokenStorageService = new TokenStorageService();
    });
    describe('setToken', () => {
        it('should set a token in memory', () => {
            const token: PossibleToken = "mock-token";
            tokenStorageService.setToken(token);
            expect(tokenStorageService.getToken()).toBe(token);
        });
    });
    describe('getToken', () => {
        it('should return a token if present', () => {
            const token: string = "mock-token";
            tokenStorageService.setToken(token);
            const possibleToken: PossibleToken = tokenStorageService.getToken();
            expect(possibleToken).toBe(token);
        });
        it('should return null when no token is set', () => {
            const possibleToken: PossibleToken = tokenStorageService.getToken();
            expect(possibleToken).toBeNull();
        });
    });
    describe('clear', () => {
        it('should clear the in-memory token', () => {
            const token: string = "mock-token";
            tokenStorageService.setToken(token);
            expect(tokenStorageService.getToken()).toBe(token);
            tokenStorageService.clear();
            expect(tokenStorageService.getToken()).toBeNull();
        });
        it('should emit null to observers on clear', (done) => {
            const seen: (string | null)[] = [];
            const subscription: Subscription = tokenStorageService.observe().subscribe(value => {
                seen.push(value as any);
                if (seen.length === 2) {
                    expect(seen[0]).toBeNull();
                    expect(seen[1]).toBeNull();
                    subscription.unsubscribe();
                    done();
                }
            });
            tokenStorageService.clear();
        });
    });
});