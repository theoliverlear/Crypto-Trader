import {inject} from '@angular/core';
import {HttpInterceptorFn} from '@angular/common/http';
import {TokenStorageService} from '../auth/token-storage.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
    const tokenStorage: TokenStorageService = inject(TokenStorageService);
    const token: string = tokenStorage.getToken();
    if (token) {
        const authRequest = request.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
        return next(authRequest);
    }
    return next(request);
};
