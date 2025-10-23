import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CupboardComponent} from './components/cupboard/cupboard.component';
import {LoginComponent} from './components/login/login.component';
import {HomepageComponent} from './components/homepage/homepage.component';
import {BasketComponent} from './components/basket/basket.component';
import { NeedInfoComponent } from './components/need-info/need-info.component';
import { ManagerDashboardComponent } from './components/manager-dashboard/manager-dashboard.component';

const routes: Routes = [
    { path: '', component: HomepageComponent, title: "Home"},
    { path: 'login', component: LoginComponent, title: "Login"},
    { path: 'cupboard', component: CupboardComponent, title: "Cupboard"},
    { path: 'dashboard', component: ManagerDashboardComponent, title: "Dashboard"},
    { path: 'basket', component: BasketComponent, title: "Basket"},
    { path: 'need/:id', component: NeedInfoComponent, title: "Need"},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}