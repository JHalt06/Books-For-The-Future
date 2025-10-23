import {Component, EventEmitter, Input, OnChanges, Output} from '@angular/core';
import {Need} from '../../models/need.model';
import {CupboardService} from '../../services/cupboard.service';
import {catchError, of} from 'rxjs';
import { ModalService } from '../../services/modal.service';

@Component({
    selector: 'app-need-edit',
    standalone: false,
    templateUrl: './need-edit.component.html',
    styleUrl: './need-edit.component.css'
})
export class NeedEditComponent implements OnChanges {

    @Input() mode!: "Create" | "Edit"
    @Input() need?: Need;
    @Output() refreshNeedList = new EventEmitter<void>();

    needCopy: any = {}

    constructor(
      private cupboardService: CupboardService,
      protected modalService: ModalService,
    ) {}

    ngOnChanges() {
      this.needCopy = {...this.need}
    }

    submit(form: any) {
      const need: Need = {
        name: form.name,
        id: this.needCopy.id,
        quantity: form.quantity,
        fundingAmount: 0,
      };

      if (this.mode == "Edit") {
        this.updateNeed(need)
      } else if (this.mode === 'Create') {
        this.createNeed(need)
      }
    }

    updateNeed(need: Need) {
      need.fundingAmount = this.need?.fundingAmount ?? 0
      this.cupboardService.updateNeed(need.id, need)
        .pipe(catchError((ex, _) => {
          return of()
        }))
        .subscribe(
          (result) => {
            if (result) {
              this.refreshNeedList.emit();
            } else {
              console.log("error");
            }
          }
        );
    }

    createNeed(need: Need) {
      this.cupboardService.createNeed(need)
        .pipe(catchError((ex, _) => {
          return of()
        }))
        .subscribe(
          (result) => {
            if (result) {
              this.refreshNeedList.emit();
            } else {
              console.log("error");
            }
          }
        );
    }
}
