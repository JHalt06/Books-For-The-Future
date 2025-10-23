import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CupboardService} from '../../services/cupboard.service';
import {Need} from '../../models/need.model';
import {catchError, of} from 'rxjs';
import {NeedListComponent} from '../need-list/need-list.component';
import {AuthService} from '../../services/auth.service';
import {UsersService} from '../../services/users.service';
import { ModalService } from '../../services/modal.service';

@Component({
    selector: 'app-cupboard',
    standalone: false,
    templateUrl: './cupboard.component.html',
    styleUrl: './cupboard.component.css'
})
export class CupboardComponent implements OnInit {

    @ViewChild("needList") needList?: NeedListComponent
    @ViewChild("searchForm") searchForm!: ElementRef<HTMLInputElement>
    needs: Need[] = [];
    searchResults: Need[] = [];
itemsPerPage: any;

    constructor(
      private cupboardService: CupboardService,
      private authService: AuthService,
      protected usersService: UsersService,
      protected modalService: ModalService,
    ) {}

    ngOnInit(): void {
      this.refresh()
    }

    refresh() {
      this.cupboardService.getNeeds().subscribe(n => {
        this.needs = n;
        this.searchResults = n;
      });
      this.searchForm.nativeElement.form?.reset()
    }

    //might need to be async
    search(search: any) {
      if (search) {
        console.log("IF BLOCK")
        this.cupboardService.searchNeeds(search).subscribe((n) => {
            this.searchResults = n;
        });
      } else {
        this.searchResults = this.needs;
      }
    }

    deleteNeed(id : number) {
      this.cupboardService.deleteNeed(id)
        .pipe(catchError((ex, _) => {
            return of()
        }))
        .subscribe(() => {
            this.refresh();
        })
    }

    addToBasket(need: Need) {
      const currentUser = this.authService.getCurrentUser();
      if (currentUser) {
        if (!currentUser.basket.includes(need.id)) {
          currentUser.basket.push(need.id);
          this.usersService.updateUser(currentUser)
            .pipe(catchError((err, _) =>  {
              return of();
            }))
            .subscribe(() => {
              this.usersService.refreshBasket();
            });
        }
      } 
    }

    protected readonly Object = Object;
}
