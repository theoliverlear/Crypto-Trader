import {
    CurrencyFormatterService
} from "../../services/currency-formatter.service";
import {TestBed} from "@angular/core/testing";

describe('CurrencyFormatterService', () => {
    let service: CurrencyFormatterService;
    beforeEach((): void => {
        TestBed.configureTestingModule({
            providers: [CurrencyFormatterService],
        });
        service = TestBed.inject(CurrencyFormatterService);
    });
    it('should be created', () => {
        expect(service).toBeTruthy();
    });
    describe('formatCurrency', () => {
        it('should format a currency', () => {
            const amount: number = 12345.67;
            const formattedAmount: string = service.formatCurrency(amount);
            expect(formattedAmount).toBe('$12,345.67');
        });
        it('should format tiny currency', () => {
            const amount: number = 0.0000459;
            const formattedAmount: string = service.formatCurrency(amount);
            expect(formattedAmount).toBe('$0.0000459');
        })
    });
});