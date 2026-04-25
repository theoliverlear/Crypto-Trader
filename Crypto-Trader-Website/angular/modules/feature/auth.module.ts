import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AngularSuiteModule } from '@theoliverlear/angular-suite';

import { UniversalModule } from '../shared/universal.module';
import { AuthConsoleComponent } from '@components/elements/element-group-auth/auth-console/auth-console.component';
import { AuthConsoleLoginSectionComponent } from '@components/elements/element-group-auth/auth-console-login-section/auth-console-login-section.component';
import { AuthConsoleSignupSectionComponent } from '@components/elements/element-group-auth/auth-console-signup-section/auth-console-signup-section.component';
import { AuthConsoleTabComponent } from '@components/elements/element-group-auth/auth-console-tab/auth-console-tab.component';
import { AuthConsoleTabSectionComponent } from '@components/elements/element-group-auth/auth-console-tab-section/auth-console-tab-section.component';
import { AuthGuardPopupComponent } from '@components/elements/element-group-auth/auth-guard-popup/auth-guard-popup.component';
import { AuthInputComponent } from '@components/elements/element-group-auth/auth-input/auth-input.component';
import { AuthorizeComponent } from '@components/pages/authorize/authorize.component';

const authComponents = [
    AuthConsoleComponent,
    AuthConsoleLoginSectionComponent,
    AuthConsoleSignupSectionComponent,
    AuthConsoleTabComponent,
    AuthConsoleTabSectionComponent,
    AuthGuardPopupComponent,
    AuthInputComponent,
    AuthorizeComponent,
];

@NgModule({
    declarations: [...authComponents],
    imports: [
        CommonModule,
        FormsModule,
        AngularSuiteModule,
        UniversalModule,
    ],
    exports: [...authComponents],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AuthModule {}
