import {Component, Inject} from '@angular/core';
import { DOCUMENT } from '@angular/common';

@Component({
    selector: 'app-homepage',
    standalone: false,
    templateUrl: './homepage.component.html',
    styleUrl: './homepage.component.css'
})
export class HomepageComponent  {

    constructor(
        @Inject(DOCUMENT) private document: Document
    ) {}

}
