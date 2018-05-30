import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BillDetailsSharedModule } from '../../shared';
import {
    BillDetailsService,
    BillDetailsPopupService,
    BillDetailsComponent,
    BillDetailsDetailComponent,
    BillDetailsDialogComponent,
    BillDetailsPopupComponent,
    BillDetailsDeletePopupComponent,
    BillDetailsDeleteDialogComponent,
    billDetailsRoute,
    billDetailsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...billDetailsRoute,
    ...billDetailsPopupRoute,
];

@NgModule({
    imports: [
        BillDetailsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BillDetailsComponent,
        BillDetailsDetailComponent,
        BillDetailsDialogComponent,
        BillDetailsDeleteDialogComponent,
        BillDetailsPopupComponent,
        BillDetailsDeletePopupComponent,
    ],
    entryComponents: [
        BillDetailsComponent,
        BillDetailsDialogComponent,
        BillDetailsPopupComponent,
        BillDetailsDeleteDialogComponent,
        BillDetailsDeletePopupComponent,
    ],
    providers: [
        BillDetailsService,
        BillDetailsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BillDetailsBillDetailsModule {}
