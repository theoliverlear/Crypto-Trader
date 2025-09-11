import {Component, OnInit} from "@angular/core";
import {AuthPopup, WebSocketCapable} from "@theoliverlear/angular-suite";
import {Subscription} from "rxjs";
import {
    SignupWebSocketService
} from "../../../services/net/websocket/signup-websocket.service";

@Component({
    selector: 'authorize',
    standalone: false,
    templateUrl: './authorize.component.html',
    styleUrls: ['./authorize.component.scss']
})
export class AuthorizeComponent implements WebSocketCapable, OnInit {
    constructor(private signupWebSocket: SignupWebSocketService) {

    }
    
    ngOnInit(): void {
        this.initializeWebSocket();
    }

    webSocketSubscription: Subscription;
    initializeWebSocket(): void {
        this.signupWebSocket.connect();
        this.webSocketSubscription = this.signupWebSocket.getMessages().subscribe(
            (message) => {
                console.log(message);
            },
            (error) => {
                console.log(error);
            },
            () => {
                console.log('complete');
            }
        );
    }

    protected readonly AuthPopup = AuthPopup;
}