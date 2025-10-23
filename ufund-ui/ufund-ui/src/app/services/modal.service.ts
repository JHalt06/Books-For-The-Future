import {Injectable, TemplateRef} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class ModalService {

    private modal = new BehaviorSubject<TemplateRef<any> | null>(null)

    constructor() {}

    showModal(template: TemplateRef<any>) {
        this.modal.next(template)
    }

    hideModal() {
        this.modal.next(null)
    }

    getModalBehaviorSubject() {
        return this.modal;
    }
}
