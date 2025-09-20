import {PossibleToken} from "../../../models/auth/types";
import {
    TokenStorageService
} from "../../../services/auth/token-storage.service";

describe('TokenStorageService', () => {
    let tokenStorageService: TokenStorageService;
    beforeEach(() => {
        sessionStorage.clear();
        localStorage.clear();
        tokenStorageService = new TokenStorageService();
    });
    describe('setToken', () => {
        it('should set a token in session storage', () => {
            const token: PossibleToken = "mock-token";
            tokenStorageService.setToken(token);
            expect(localStorage.getItem('ct_jwt')).toBe(token);
        });
    });
    describe('getToken', () => {
        it('should return a token if present', () => {
            const token: string = "mock-token";
            tokenStorageService.setToken(token);
            const possibleToken: PossibleToken = tokenStorageService.getToken();
            expect(possibleToken).toBe(token);
        });
    });
    describe('clear', () => {
        it('should clear session storage', () => {
            const token: string = "mock-token";
            tokenStorageService.setToken(token);
            expect(localStorage.getItem('ct_jwt')).toBe(token);
            tokenStorageService.clear();
            expect(localStorage.getItem('ct_jwt')).toBeNull();
        });
    });
});