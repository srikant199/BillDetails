import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BillDetails } from './bill-details.model';
import { BillDetailsPopupService } from './bill-details-popup.service';
import { BillDetailsService } from './bill-details.service';

@Component({
    selector: 'jhi-bill-details-delete-dialog',
    templateUrl: './bill-details-delete-dialog.component.html'
})
export class BillDetailsDeleteDialogComponent {

    billDetails: BillDetails;

    constructor(
        private billDetailsService: BillDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.billDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'billDetailsListModification',
                content: 'Deleted an billDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bill-details-delete-popup',
    template: ''
})
export class BillDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private billDetailsPopupService: BillDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.billDetailsPopupService
                .open(BillDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
