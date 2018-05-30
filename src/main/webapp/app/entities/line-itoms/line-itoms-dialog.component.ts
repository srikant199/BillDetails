import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LineItoms } from './line-itoms.model';
import { LineItomsPopupService } from './line-itoms-popup.service';
import { LineItomsService } from './line-itoms.service';
import { BillDetails, BillDetailsService } from '../bill-details';

@Component({
    selector: 'jhi-line-itoms-dialog',
    templateUrl: './line-itoms-dialog.component.html'
})
export class LineItomsDialogComponent implements OnInit {

    lineItoms: LineItoms;
    isSaving: boolean;

    billdetails: BillDetails[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private lineItomsService: LineItomsService,
        private billDetailsService: BillDetailsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.billDetailsService.query()
            .subscribe((res: HttpResponse<BillDetails[]>) => { this.billdetails = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.lineItoms.id !== undefined) {
            this.subscribeToSaveResponse(
                this.lineItomsService.update(this.lineItoms));
        } else {
            this.subscribeToSaveResponse(
                this.lineItomsService.create(this.lineItoms));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<LineItoms>>) {
        result.subscribe((res: HttpResponse<LineItoms>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: LineItoms) {
        this.eventManager.broadcast({ name: 'lineItomsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBillDetailsById(index: number, item: BillDetails) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-line-itoms-popup',
    template: ''
})
export class LineItomsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lineItomsPopupService: LineItomsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.lineItomsPopupService
                    .open(LineItomsDialogComponent as Component, params['id']);
            } else {
                this.lineItomsPopupService
                    .open(LineItomsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
