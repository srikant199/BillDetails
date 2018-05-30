import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LineItoms } from './line-itoms.model';
import { LineItomsPopupService } from './line-itoms-popup.service';
import { LineItomsService } from './line-itoms.service';

@Component({
    selector: 'jhi-line-itoms-delete-dialog',
    templateUrl: './line-itoms-delete-dialog.component.html'
})
export class LineItomsDeleteDialogComponent {

    lineItoms: LineItoms;

    constructor(
        private lineItomsService: LineItomsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.lineItomsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'lineItomsListModification',
                content: 'Deleted an lineItoms'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-line-itoms-delete-popup',
    template: ''
})
export class LineItomsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lineItomsPopupService: LineItomsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.lineItomsPopupService
                .open(LineItomsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
