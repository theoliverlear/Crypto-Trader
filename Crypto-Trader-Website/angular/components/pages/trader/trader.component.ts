// trader.component.ts 
import {
    Component,
    HostListener,
    OnChanges,
    OnInit,
    SimpleChanges
} from "@angular/core";
import {
    AllTradeEventsService
} from "../../../services/net/http/trader/all-trade-events.service";
import {TradeEvent, TradeEventList} from "../../../models/trader/types";
import {TagType} from "@theoliverlear/angular-suite";
import { listStagger } from "../../animations/animations";
import {
    BatchedTradeEventsService
} from "../../../services/net/http/trader/batched-trade-events.service";
import {
    HasTradeEventsService
} from "../../../services/net/http/trader/has-trade-events.service";

@Component({
    selector: 'trader',
    standalone: false,
    templateUrl: './trader.component.html',
    styleUrls: ['./trader.component.scss'],
    animations: [listStagger]
})
export class TraderComponent implements OnInit, OnChanges {
    tradeEvents: TradeEventList = {
        events: []
    };
    tradeEventIds: number[] = [];
    private loadingStatus: Record<number, boolean> = {};
    isLoaded: boolean = false;
    isFetching: boolean = false;
    loadingNewItems: boolean = false;
    hasTradeEvents: boolean | undefined = undefined;
    private offset: number = 0;
    private readonly pageSize: number = 10;
    hasMore: boolean = true;
    private scrollTimer: any = null;
    constructor(private batchedTradeEventsService: BatchedTradeEventsService,
                private hasTradeEventsService: HasTradeEventsService) {
        
    }
    ngOnInit(): void {
        this.hasTradeEventsService.hasTradeEvents().subscribe((hasTradeEvents: boolean) => {
            this.hasTradeEvents = hasTradeEvents;
        });
        this.initializeTradeEvents();
        this.loadEvents(0);
    }
    

    @HostListener('window:scroll', [])
    onScroll() {
        if (this.scrollTimer) {
            clearTimeout(this.scrollTimer);
        }
        this.scrollTimer = setTimeout(() => {
            this.scrollTimer = null;
            if (this.loadingNewItems || this.isFetching || !this.hasMore) {
                return;
            }
            const scrollPosition: number = window.scrollY + window.innerHeight;
            const pageHeight: number = document.documentElement.scrollHeight;
            if (this.shouldLoadMoreTradeEvents(scrollPosition, pageHeight)) {
                this.triggerLoadMoreTradeEvents();
            }
        }, 150);
    }

    listenForTradeEvents(): void {
        this.batchedTradeEventsService.getBatchedTradeEvents(0, this.pageSize).subscribe((data: TradeEventList) => {
            this.tradeEvents = data;
        });
    }
    
    private loadEvents(offset: number): void {
        this.isFetching = true;
        this.batchedTradeEventsService.getBatchedTradeEvents(offset, this.pageSize).subscribe({
            next: (data: TradeEventList) => {
                this.tradeEvents.events = [
                    ...(this.tradeEvents.events || []),
                    ...(data?.events || [])
                ];
                const received: number = data?.events?.length || 0;
                if (received === this.pageSize) {
                    this.offset += this.pageSize;
                } else {
                    this.hasMore = false;
                }
                this.isLoaded = true;
                this.isFetching = false;
            },
            error: () => {
                this.isFetching = false;
            }
        })
    }
    
    shouldLoadMoreTradeEvents(scrollPosition: number, pageHeight: number): boolean {
        return scrollPosition >= pageHeight - 100;
    }
    
    private triggerLoadMoreTradeEvents(): void {
        if (this.isFetching) {
            return;
        }
        this.isFetching = true;
        this.loadEvents(this.offset);
    }
    
    onTradeEventLoaded(tradeEventId: number): void {
        if (this.loadingStatus.hasOwnProperty(tradeEventId)) {
            this.loadingStatus[tradeEventId] = true;
        }
        if (this.allTradeEventsLoaded() && this.isFetching) {
            this.isFetching = false;
        }
        if (!this.isLoaded && this.initialTradeEventsLoaded()) {
            this.isLoaded = true;
        }
    }
    
    private allTradeEventsLoaded(): boolean {
        return Object.values(this.loadingStatus).every((value: boolean): boolean => value);
    }

    private initialTradeEventsLoaded(): boolean {
        const initialIds: number[] = this.tradeEventIds.slice(0, 10);
        return initialIds.every((id: number): boolean => this.loadingStatus[id]);
    }
    
    private initializeTradeEvents(): void {
        for (let i: number = 0; i < 10; i++) {
            this.tradeEventIds.push(i);
            this.loadingStatus[i] = false;
        }
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
