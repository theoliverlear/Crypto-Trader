// chat-input.component.ts
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
    selector: 'chat-input',
    templateUrl: './chat-input.component.html',
    styleUrls: ['./chat-input.component.scss'],
    standalone: false,
})
export class ChatInputComponent {
    @Input() public value: string = '';
    @Output() public valueChange: EventEmitter<string> = new EventEmitter<string>();
    @Output() public send: EventEmitter<void> = new EventEmitter<void>();
    @Output() public keyDown: EventEmitter<KeyboardEvent> = new EventEmitter<KeyboardEvent>();

    public onInput(event: Event): void {
        const target: HTMLTextAreaElement = event.target as HTMLTextAreaElement;
        this.value = target.value;
        this.valueChange.emit(this.value);
    }

    public onKeyDown(event: KeyboardEvent): void {
        this.keyDown.emit(event);
    }

    public onSend(): void {
        this.send.emit();
    }
}
