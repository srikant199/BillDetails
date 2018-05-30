/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { BillDetailsTestModule } from '../../../test.module';
import { LineItomsDialogComponent } from '../../../../../../main/webapp/app/entities/line-itoms/line-itoms-dialog.component';
import { LineItomsService } from '../../../../../../main/webapp/app/entities/line-itoms/line-itoms.service';
import { LineItoms } from '../../../../../../main/webapp/app/entities/line-itoms/line-itoms.model';
import { BillDetailsService } from '../../../../../../main/webapp/app/entities/bill-details';

describe('Component Tests', () => {

    describe('LineItoms Management Dialog Component', () => {
        let comp: LineItomsDialogComponent;
        let fixture: ComponentFixture<LineItomsDialogComponent>;
        let service: LineItomsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BillDetailsTestModule],
                declarations: [LineItomsDialogComponent],
                providers: [
                    BillDetailsService,
                    LineItomsService
                ]
            })
            .overrideTemplate(LineItomsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LineItomsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LineItomsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new LineItoms(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.lineItoms = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'lineItomsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new LineItoms();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.lineItoms = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'lineItomsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
