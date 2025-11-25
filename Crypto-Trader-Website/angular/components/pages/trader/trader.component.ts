// trader.component.ts 
import {Component, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import {
    AllTradeEventsService
} from "../../../services/net/http/trader/all-trade-events.service";
import {TradeEvent, TradeEventList} from "../../../models/trader/types";
import {TagType} from "@theoliverlear/angular-suite";

@Component({
    selector: 'trader',
    standalone: false,
    templateUrl: './trader.component.html',
    styleUrls: ['./trader.component.scss']
})
export class TraderComponent implements OnInit, OnChanges {
    tradeEvents: TradeEventList = {
        events: []
    };
    constructor(private allTradeEventsService: AllTradeEventsService) {
        
    }
    ngOnInit(): void {
        this.allTradeEventsService.getAllTradeEvents().subscribe((data: TradeEventList) => {
            this.tradeEvents = data || {
                events: []
            };
            this.tradeEvents.events.reverse();
        });
    }
    
    ngOnChanges(changes: SimpleChanges): void {
        if (changes['tradeEvents']) {
            this.tradeEvents = changes['tradeEvents'].currentValue;
        }
    }
    
    getTradeEvents(): TradeEvent[] {
        return this.tradeEvents.events;
    }

    protected readonly TagType = TagType;
}
