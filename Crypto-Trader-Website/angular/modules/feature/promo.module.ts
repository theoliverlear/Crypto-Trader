import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { AngularSuiteModule } from '@theoliverlear/angular-suite';

import { UniversalModule } from '../shared/universal.module';
import { ArchitecturePromoComponent } from 'angular/components/elements/element-group-promo/architecture-promo/architecture-promo.component';
import { HeroPromoComponent } from 'angular/components/elements/element-group-promo/hero-promo/hero-promo.component';
import { InfraPromoComponent } from 'angular/components/elements/element-group-promo/infra-promo/infra-promo.component';
import { InterfacePromoComponent } from 'angular/components/elements/element-group-promo/interface-promo/interface-promo.component';
import { MeetCryptoTraderPromoComponent } from 'angular/components/elements/element-group-promo/meet-crypto-trader-promo/meet-crypto-trader-promo.component';
import { PaperModePromoComponent } from 'angular/components/elements/element-group-promo/paper-mode-promo/paper-mode-promo.component';
import { PersonaSelectorComponent } from 'angular/components/elements/element-group-promo/persona-selector/persona-selector.component';
import { PipelinePromoComponent } from 'angular/components/elements/element-group-promo/pipeline-promo/pipeline-promo.component';
import { ServicesPromoComponent } from 'angular/components/elements/element-group-promo/services-promo/services-promo.component';
import { StartTradingPromoComponent } from 'angular/components/elements/element-group-promo/start-trading-promo/start-trading-promo.component';
import { TierPromoStripeComponent } from 'angular/components/elements/element-group-promo/tier-promo-stripe/tier-promo-stripe.component';
import { TierPromoComponent } from 'angular/components/elements/element-group-promo/tier-promo/tier-promo.component';
import { TransparencyPromoComponent } from 'angular/components/elements/element-group-promo/transparency-promo/transparency-promo.component';

const promoComponents = [
    ArchitecturePromoComponent,
    HeroPromoComponent,
    InfraPromoComponent,
    InterfacePromoComponent,
    MeetCryptoTraderPromoComponent,
    PaperModePromoComponent,
    PersonaSelectorComponent,
    PipelinePromoComponent,
    ServicesPromoComponent,
    StartTradingPromoComponent,
    TierPromoComponent,
    TierPromoStripeComponent,
    TransparencyPromoComponent,
];

@NgModule({
    declarations: [...promoComponents],
    imports: [
        CommonModule,
        AngularSuiteModule,
        UniversalModule,
    ],
    exports: [...promoComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class PromoModule {}
