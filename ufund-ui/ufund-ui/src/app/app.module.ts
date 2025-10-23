import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NeedInfoComponent} from './components/need-info/need-info.component';
import {HomepageComponent} from './components/homepage/homepage.component';
import {BasketComponent} from './components/basket/basket.component';
import {CupboardComponent} from './components/cupboard/cupboard.component';
// import {NeedListComponent} from './components/need-list/need-list.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {ManagerDashboardComponent} from './components/manager-dashboard/manager-dashboard.component';
import {AsyncPipe, CommonModule} from '@angular/common';
import {LoginComponent} from './components/login/login.component';
import { NeedEditComponent } from './components/need-edit/need-edit.component';
import { NeedListComponent } from './components/need-list/need-list.component';

@NgModule({
    declarations: [
        AppComponent,
        NeedInfoComponent,
        HomepageComponent,
        BasketComponent,
        CupboardComponent,
        ManagerDashboardComponent,
        LoginComponent,
        NeedEditComponent,
        NeedListComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        RouterLink,
        RouterLinkActive,
        RouterOutlet,
        CommonModule,
        HttpClientModule,
        AsyncPipe
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
