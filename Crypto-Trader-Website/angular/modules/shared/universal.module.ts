import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { AngularSuiteModule } from '@theoliverlear/angular-suite';
import {
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatOption,
} from '@angular/material/autocomplete';
import { MatIconButton } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatFormField, MatInput, MatSuffix } from '@angular/material/input';

import { AccordionComponent } from '@components/elements/element-group-system/accordion/accordion.component';
import { ArrowComponent } from '@components/elements/element-group-system/arrow/arrow.component';
import { BadgeGridComponent } from '@components/elements/element-group-system/badge-grid/badge-grid.component';
import { CodeWindowComponent } from '@components/elements/element-group-system/code-window/code-window.component';
import { ContentStripeComponent } from '@components/elements/element-group-system/content-stripe/content-stripe.component';
import { CtaButtonComponent } from '@components/elements/element-group-system/cta-button/cta-button.component';
import { EyebrowLabelComponent } from '@components/elements/element-group-system/eyebrow-label/eyebrow-label.component';
import { IconTextItemComponent } from '@components/elements/element-group-system/icon-text-item/icon-text-item.component';
import { LoadingWheelComponent } from '@components/elements/element-group-system/loading-wheel/loading-wheel.component';
import { PageTitleStripeComponent } from '@components/elements/element-group-system/page-title-stripe/page-title-stripe.component';
import { PopupComponent } from '@components/elements/element-group-system/popup/popup.component';
import { SimpleSortIconComponent } from '@components/elements/element-group-system/simple-sort-icon/simple-sort-icon.component';
import { StatStripComponent } from '@components/elements/element-group-system/stat-strip/stat-strip.component';
import { FlipWordsComponent } from '@components/elements/element-group-animated/flip-words/flip-words.component';
import { ScrollRevealComponent } from '@components/elements/element-group-animated/scroll-reveal/scroll-reveal.component';
import { SearchInputComponent } from '@components/elements/element-group-input/search-input/search-input.component';
import { ProfilePictureComponent } from '@components/elements/element-group-profile/profile-picture/profile-picture.component';

const universalComponents = [
    AccordionComponent,
    ArrowComponent,
    BadgeGridComponent,
    CodeWindowComponent,
    ContentStripeComponent,
    CtaButtonComponent,
    EyebrowLabelComponent,
    FlipWordsComponent,
    IconTextItemComponent,
    LoadingWheelComponent,
    PageTitleStripeComponent,
    PopupComponent,
    ProfilePictureComponent,
    ScrollRevealComponent,
    SearchInputComponent,
    SimpleSortIconComponent,
    StatStripComponent,
];

@NgModule({
    declarations: [...universalComponents],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule,
        AngularSuiteModule,
        MatAutocomplete,
        MatAutocompleteTrigger,
        MatExpansionModule,
        MatFormField,
        MatIconButton,
        MatIconModule,
        MatInput,
        MatOption,
        MatSuffix,
    ],
    exports: [...universalComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class UniversalModule {}
