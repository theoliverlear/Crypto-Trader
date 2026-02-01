import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { HttpClientService } from '@theoliverlear/angular-suite';
import { environment } from '@environments/environment';
import {
    ConsoleCommandRequest,
    ConsoleCommandResponse,
} from '@models/console/types';

@Injectable({
    providedIn: 'root',
})
export class ConsoleCommandService extends HttpClientService<
    ConsoleCommandRequest,
    ConsoleCommandResponse
> {
    private static readonly URL: string = `${environment.apiUrl}/console/execute`;

    constructor() {
        super(ConsoleCommandService.URL);
    }

    public executeCommand(
        command: ConsoleCommandRequest,
    ): Observable<ConsoleCommandResponse> {
        return this.post(command);
    }
}
