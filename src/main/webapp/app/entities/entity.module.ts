import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { BillDetailsBillDetailsModule } from './bill-details/bill-details.module';
import { BillDetailsLineItomsModule } from './line-itoms/line-itoms.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        BillDetailsBillDetailsModule,
        BillDetailsLineItomsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BillDetailsEntityModule {}
