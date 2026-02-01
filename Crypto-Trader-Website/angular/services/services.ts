import { SwipeService } from '@ui/swipe.service';

import { ErrorHandlerService } from './error-handler.service';
import { AuthGuard } from './guard/auth.guard';

export const services = [
    // Guards
    AuthGuard,
    // Services
    ErrorHandlerService,
    SwipeService,
];
