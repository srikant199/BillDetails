import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { BillDetails } from './bill-details.model';
import { BillDetailsService } from './bill-details.service';

@Injectable()
export class BillDetailsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private billDetailsService: BillDetailsService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.billDetailsService.find(id)
                    .subscribe((billDetailsResponse: HttpResponse<BillDetails>) => {
                        const billDetails: BillDetails = billDetailsResponse.body;
                        billDetails.date = this.datePipe
                            .transform(billDetails.date, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.billDetailsModalRef(component, billDetails);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.billDetailsModalRef(component, new BillDetails());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    billDetailsModalRef(component: Component, billDetails: BillDetails): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.billDetails = billDetails;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
