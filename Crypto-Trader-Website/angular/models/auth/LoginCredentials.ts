import {FilledFieldsService} from "@theoliverlear/angular-suite";
import {LoginRequest} from "./types";

export class LoginCredentials {
    email: string;
    password: string;
    constructor(email: string = "",
                password: string = "") {
        this.email = email;
        this.password = password;
    }
    
    public isFilledFields(): boolean {
        const filledFieldsService: FilledFieldsService = new FilledFieldsService();
        return filledFieldsService.isFilledFields([this.email, this.password]);
    }
    
    public getRequest(): LoginRequest {
        if (!this.isFilledFields()) {
            throw new Error("Cannot get login request due to input issues.");
        }
        return {
            email: this.email,
            password: this.password
        };
    }
}