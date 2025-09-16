import {FilledFieldsService} from "@theoliverlear/angular-suite";

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
}