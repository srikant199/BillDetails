import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { LineItoms } from './line-itoms.model';
import { LineItomsService } from './line-itoms.service';

@Component({
    selector: 'jhi-line-itoms-detail',
    templateUrl: './line-itoms-detail.component.html'
})
export class LineItomsDetailComponent implements OnInit, OnDestroy {

    lineItoms: LineItoms;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private lineItomsService: LineItomsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLineItoms();
    }

    load(id) {
        this.lineItomsService.find(id)
            .subscribe((lineItomsResponse: HttpResponse<LineItoms>) => {
                this.lineItoms = lineItomsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLineItoms() {
        this.eventSubscriber = this.eventManager.subscribe(
            'lineItomsListModification',
            (response) => this.load(this.lineItoms.id)
        );
    }
}
