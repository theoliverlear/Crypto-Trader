import {AuthGuard} from "./guard/auth.guard";
import {ErrorHandlerService} from "./error-handler.service";
import {HashPasswordService} from "./hash-password.service";
import {PasswordMatchService} from "./password-match.service";
import {SwipeService} from "./swipe.service";
import {LoginService} from "./server/login.service";
import {SignupService} from "./server/signup.service";
import {UserService} from "./server/user.service";
import {EmailValidatorService} from "./email-validator.service";
import {FilledFieldsService} from "./filled-fields.service";

export const services = [
    // Guards
    AuthGuard,
    // Sever Services
    LoginService,
    SignupService,
    UserService,
    // Services
    EmailValidatorService,
    ErrorHandlerService,
    FilledFieldsService,
    HashPasswordService,
    PasswordMatchService,
    SwipeService
]