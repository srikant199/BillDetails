/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BillDetailsTestModule } from '../../../test.module';
import { BillDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/bill-details/bill-details-detail.component';
import { BillDetailsService } from '../../../../../../main/webapp/app/entities/bill-details/bill-details.service';
import { BillDetails } from '../../../../../../main/webapp/app/entities/bill-details/bill-details.model';

describe('Component Tests', () => {

    describe('BillDetails Management Detail Component', () => {
        let comp: BillDetailsDetailComponent;
        let fixture: ComponentFixture<BillDetailsDetailComponent>;
        let service: BillDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BillDetailsTestModule],
                declarations: [BillDetailsDetailComponent],
                providers: [
                    BillDetailsService
                ]
            })
            .overrideTemplate(BillDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BillDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new BillDetails(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.billDetails).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
