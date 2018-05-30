/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BillDetailsTestModule } from '../../../test.module';
import { BillDetailsComponent } from '../../../../../../main/webapp/app/entities/bill-details/bill-details.component';
import { BillDetailsService } from '../../../../../../main/webapp/app/entities/bill-details/bill-details.service';
import { BillDetails } from '../../../../../../main/webapp/app/entities/bill-details/bill-details.model';

describe('Component Tests', () => {

    describe('BillDetails Management Component', () => {
        let comp: BillDetailsComponent;
        let fixture: ComponentFixture<BillDetailsComponent>;
        let service: BillDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BillDetailsTestModule],
                declarations: [BillDetailsComponent],
                providers: [
                    BillDetailsService
                ]
            })
            .overrideTemplate(BillDetailsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BillDetailsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BillDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new BillDetails(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.billDetails[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
