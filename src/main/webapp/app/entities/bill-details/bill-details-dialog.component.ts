import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BillDetails } from './bill-details.model';
import { BillDetailsPopupService } from './bill-details-popup.service';
import { BillDetailsService } from './bill-details.service';

@Component({
    selector: 'jhi-bill-details-dialog',
    templateUrl: './bill-details-dialog.component.html'
})
export class BillDetailsDialogComponent implements OnInit {

    billDetails: BillDetails;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private billDetailsService: BillDetailsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.billDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.billDetailsService.update(this.billDetails));
        } else {
            this.subscribeToSaveResponse(
                this.billDetailsService.create(this.billDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<BillDetails>>) {
        result.subscribe((res: HttpResponse<BillDetails>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: BillDetails) {
        this.eventManager.broadcast({ name: 'billDetailsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-bill-details-popup',
    template: ''
})
export class BillDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private billDetailsPopupService: BillDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.billDetailsPopupService
                    .open(BillDetailsDialogComponent as Component, params['id']);
            } else {
                this.billDetailsPopupService
                    .open(BillDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
