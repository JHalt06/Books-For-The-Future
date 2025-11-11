import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CupboardService} from '../../services/cupboard.service';
import {Need} from '../../models/need.model';
import {catchError, of} from 'rxjs';
import {NeedListComponent} from '../need-list/need-list.component';
import {AuthService} from '../../services/auth.service';
import {UsersService} from '../../services/users.service';
import { ModalService } from '../../services/modal.service';
import { HttpClient } from '@angular/common/http';
import { interval, Subscription } from 'rxjs';

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
    selectedFilter: string = '';
    itemsPerPage: any;

    constructor(
      private http: HttpClient,
      private cupboardService: CupboardService,
      private authService: AuthService,
      protected usersService: UsersService,
      protected modalService: ModalService,
    ) {}

    ngOnInit(): void {
      this.loadNeeds()
      // this.refresh()
    }


    refresh() {
      this.cupboardService.getNeeds().subscribe(n => {
        this.needs = n;
        this.searchResults = n;
      });
      if (this.searchForm) {
        this.searchForm.nativeElement.form?.reset()
      }
    }

    // refresh() {
    //   this.loadNeeds();
    //   @ViewChild("needList") needList?: NeedListComponent
    //   @ViewChild("searchForm") searchForm!: ElementRef<HTMLInputElement>
    //   needs: Need[] = [];
    //   searchResults: Need[] = [];
    //   itemsPerPage: any;
    // }

  // ngOnInit(): void {
  //   this.refresh()
  // }


  loadNeeds(): void {
      let url = 'http://localhost:8080/cupboard';
      if(this.selectedFilter) {
        url += `?filter=${this.selectedFilter}`;
      }

      this.http.get<Need[]>(url).subscribe({
        next: (data) => {
          this.needs = data;
          this.searchResults = data;
        },
        error: (err) => {
          console.error('Error fetching needs:', err);
        }
      });
    }

    applyFilter(): void {
      this.loadNeeds();
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

  onSearch(query: string | null) {
    if (query && query.trim() !== '') {
      this.cupboardService.searchNeeds(query).subscribe({
        next: (n) => {
          this.searchResults = n;
        },
        error: (err) => {
          // If the API returns 404 (Not Found), it means no results.
          if (err.status === 404 || err.status === 200) { // Some APIs might return 200 with empty array for no results
            this.searchResults = [];
          }
        }
      });
    } else {
      this.searchResults = this.needs;
    }
  }

  clearSearch() {
    if(this.searchForm) {
      this.searchForm.nativeElement.value = '';
    }
    this.searchResults = this.needs;
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
