/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { BillDetailsTestModule } from '../../../test.module';
import { BillDetailsDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/bill-details/bill-details-delete-dialog.component';
import { BillDetailsService } from '../../../../../../main/webapp/app/entities/bill-details/bill-details.service';

describe('Component Tests', () => {

    describe('BillDetails Management Delete Component', () => {
        let comp: BillDetailsDeleteDialogComponent;
        let fixture: ComponentFixture<BillDetailsDeleteDialogComponent>;
        let service: BillDetailsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BillDetailsTestModule],
                declarations: [BillDetailsDeleteDialogComponent],
                providers: [
                    BillDetailsService
                ]
            })
            .overrideTemplate(BillDetailsDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BillDetailsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillDetailsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
