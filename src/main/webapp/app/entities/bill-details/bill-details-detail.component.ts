import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { BillDetails } from './bill-details.model';
import { BillDetailsService } from './bill-details.service';

@Component({
    selector: 'jhi-bill-details-detail',
    templateUrl: './bill-details-detail.component.html'
})
export class BillDetailsDetailComponent implements OnInit, OnDestroy {

    billDetails: BillDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private billDetailsService: BillDetailsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBillDetails();
    }

    load(id) {
        this.billDetailsService.find(id)
            .subscribe((billDetailsResponse: HttpResponse<BillDetails>) => {
                this.billDetails = billDetailsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBillDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'billDetailsListModification',
            (response) => this.load(this.billDetails.id)
        );
    }
}
