import {Component, Input, OnChanges, TemplateRef} from '@angular/core';
import {Need} from '../../models/need.model';

@Component({
  selector: 'app-need-list',
  standalone: false,
  templateUrl: './need-list.component.html',
  styleUrl: './need-list.component.css'
})
export class NeedListComponent implements OnChanges {

  @Input({required: true}) needs!: Need[]
  @Input({required: true}) uid!: number
  @Input() itemsPerPage: number = 5;
  @Input() actionArea: TemplateRef<any> | null = null

  visibleNeeds: Need[] = [];
  currentPage: number = parseInt(localStorage.getItem('currentPage'+this.uid) ?? '0') ?? 0;
  totalPages: number = 0;

  ngOnChanges() {
    if (this.needs && this.needs.length > 0) {
      this.currentPage = parseInt(localStorage.getItem('currentPage'+this.uid) ?? '0') ?? 0;
      this.updateVisibleNeeds();
    } else {
      this.visibleNeeds = [];
    }
  }

  previousNeedsLength: number = 0;

  ngDoCheck() {
    if (this.needs && this.needs.length !== this.previousNeedsLength) {
      this.previousNeedsLength = this.needs.length;
      this.updateVisibleNeeds();
    }
  }

  updateVisibleNeeds() {
    this.totalPages = Math.ceil(this.needs.length / this.itemsPerPage);

    if (this.itemsPerPage === Infinity) {
      this.visibleNeeds = this.needs;
      this.totalPages = 1;
      return;
    }

    if (this.currentPage >= this.totalPages) {
      this.currentPage = this.totalPages - 1;
    }
    if (this.currentPage < 0) {
      this.currentPage = 0;
    }
    const start = this.currentPage * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.visibleNeeds = this.needs.slice(start, end);
  }

  decrementPage() {
    this.currentPage--;
    localStorage.setItem('currentPage'+this.uid, this.currentPage.toString());
    this.updateVisibleNeeds();
  }

  incrementPage() {
    this.currentPage++;
    localStorage.setItem('currentPage'+this.uid, this.currentPage.toString());
    this.updateVisibleNeeds();
  }

  lastPage() {
    this.currentPage = this.totalPages - 1
    localStorage.setItem('currentPage'+this.uid, this.currentPage.toString());
    this.updateVisibleNeeds()
  }

  firstPage() {
    this.currentPage = 0
    localStorage.setItem('currentPage'+this.uid, this.currentPage.toString());
    this.updateVisibleNeeds()
  }

  protected readonly Infinity = Infinity;
}

