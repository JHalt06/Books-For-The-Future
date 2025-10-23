import {Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {UsersService} from '../../services/users.service';
import {CupboardService} from '../../services/cupboard.service';
import {BehaviorSubject, firstValueFrom, of} from 'rxjs';
import {AuthService} from '../../services/auth.service';
import { UserRole } from '../../models/user.model';

@Component({
  selector: 'app-basket',
  standalone: false,
  templateUrl: './basket.component.html',
  styleUrl: './basket.component.css'
})
export class BasketComponent implements OnInit {

    constructor(
      protected cupboardService: CupboardService,
      protected usersService: UsersService,
      protected authService: AuthService,
    ) {}

    protected fundingTotal = new BehaviorSubject(0)
    protected physicalTotal: string[] = []
    protected cancelModalFn?: () => void
    protected acceptModalFn?: () => void

    @ViewChild("donation") donation?: Input;

    ngOnInit(): void {
      this.usersService.refreshBasket();

      this.usersService.getBasket().subscribe(basket => {
        console.log("Basket contents:", basket);
      });
    }

  async checkout() {
    let cart: { needID: number, quantity: number }[] = []
    let invalid = false

    this.fundingTotal.next(0);
    this.physicalTotal = []

    for (let donation of document.querySelectorAll<HTMLInputElement>('.donation')!) {
      cart.push({needID: +donation.id, quantity: donation.valueAsNumber});
    }

    if (invalid) {
      return;
    }

    try {
      await firstValueFrom(this.cupboardService.checkoutNeed(cart))
    } catch (ex:any) {
      return
    }

    cart.forEach(n => this.usersService.removeNeed(n.needID))
  }

  onInput(n: any) {
    let total = 0
    this.fundingTotal.next(total);
    this.physicalTotal = []
    for (let donation of document.querySelectorAll<HTMLInputElement>('.donation')!) {
      this.cupboardService.getNeed(+donation.id).subscribe(need => {
        if (donation.value != '' && donation.valueAsNumber >= 0) {
          total += donation.valueAsNumber
        }
        this.fundingTotal.next(total);
      })
    }
  }

  protected readonly UserRole = UserRole;
  protected readonly "Infinity" = Infinity
}
