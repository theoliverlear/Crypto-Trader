import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';

import { AngularSuiteModule } from '@theoliverlear/angular-suite';

import { UniversalModule } from '../shared/universal.module';
import { BuyTypeSelectorComponent } from 'angular/components/elements/element-group-trade/buy-type-selector/buy-type-selector.component';
import { BuyTypeComponent } from 'angular/components/elements/element-group-trade/buy-type/buy-type.component';
import { TradeCheckoutConfirmPanelComponent } from 'angular/components/elements/element-group-trade/trade-checkout-confirm-panel/trade-checkout-confirm-panel.component';
import { TradeCheckoutDetailsComponent } from 'angular/components/elements/element-group-trade/trade-checkout-details/trade-checkout-details.component';
import { TradeCheckoutComponent } from 'angular/components/elements/element-group-trade/trade-checkout/trade-checkout.component';
import { TradeConsoleComponent } from 'angular/components/elements/element-group-trade/trade-console/trade-console.component';
import TradeCurrencyComponent from 'angular/components/elements/element-group-trade/trade-currency/trade-currency.component';
import { VendorOptionComponent } from 'angular/components/elements/element-group-trade/vendor-option/vendor-option.component';
import { VendorOptionsComponent } from 'angular/components/elements/element-group-trade/vendor-options/vendor-options.component';
import { TradeComponent } from 'angular/components/pages/trade/trade.component';

const tradeComponents = [
    BuyTypeComponent,
    BuyTypeSelectorComponent,
    TradeCheckoutComponent,
    TradeCheckoutConfirmPanelComponent,
    TradeCheckoutDetailsComponent,
    TradeConsoleComponent,
    TradeCurrencyComponent,
    TradeComponent,
    VendorOptionComponent,
    VendorOptionsComponent,
];

@NgModule({
    declarations: [...tradeComponents],
    imports: [
        CommonModule,
        FormsModule,
        MatSelectModule,
        AngularSuiteModule,
        UniversalModule,
    ],
    exports: [...tradeComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class TradeModule {}
