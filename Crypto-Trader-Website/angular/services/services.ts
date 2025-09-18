import {AuthGuard} from "./guard/auth.guard";
import {ErrorHandlerService} from "./error-handler.service";
import {SwipeService} from "./ui/swipe.service";

export const services = [
    // Guards
    AuthGuard,
    // Services
    ErrorHandlerService,
    SwipeService
]