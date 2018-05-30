import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BillDetailsSharedModule } from '../../shared';
import {
    LineItomsService,
    LineItomsPopupService,
    LineItomsComponent,
    LineItomsDetailComponent,
    LineItomsDialogComponent,
    LineItomsPopupComponent,
    LineItomsDeletePopupComponent,
    LineItomsDeleteDialogComponent,
    lineItomsRoute,
    lineItomsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...lineItomsRoute,
    ...lineItomsPopupRoute,
];

@NgModule({
    imports: [
        BillDetailsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LineItomsComponent,
        LineItomsDetailComponent,
        LineItomsDialogComponent,
        LineItomsDeleteDialogComponent,
        LineItomsPopupComponent,
        LineItomsDeletePopupComponent,
    ],
    entryComponents: [
        LineItomsComponent,
        LineItomsDialogComponent,
        LineItomsPopupComponent,
        LineItomsDeleteDialogComponent,
        LineItomsDeletePopupComponent,
    ],
    providers: [
        LineItomsService,
        LineItomsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BillDetailsLineItomsModule {}
