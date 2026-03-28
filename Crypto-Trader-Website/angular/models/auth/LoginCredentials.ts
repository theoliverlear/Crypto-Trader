import {
    FilledFieldsService,
    HashPasswordService,
} from '@theoliverlear/angular-suite';

import { type LoginRequest } from './types';

export class LoginCredentials {
    private _email: string;
    private _password: string;
    constructor(email: string = '', password: string = '') {
        this._email = email;
        this._password = password;
    }

    public get email(): string {
        return this._email;
    }

    public set email(email: string) {
        this._email = email;
    }

    public get password(): string {
        return this._password;
    }

    public set password(password: string) {
        this._password = password;
    }

    public isFilledFields(): boolean {
        const filledFieldsService: FilledFieldsService = new FilledFieldsService();
        return filledFieldsService.isFilledFields([this.email, this.password]);
    }

    public getRequest(): LoginRequest {
        if (!this.isFilledFields()) {
            throw new Error('Cannot get login request due to input issues.');
        }
        const passwordHasher: HashPasswordService = new HashPasswordService();
        return {
            email: this.email,
            password: passwordHasher.hashPassword(this.password),
        };
    }
}
