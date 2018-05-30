/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BillDetailsTestModule } from '../../../test.module';
import { LineItomsComponent } from '../../../../../../main/webapp/app/entities/line-itoms/line-itoms.component';
import { LineItomsService } from '../../../../../../main/webapp/app/entities/line-itoms/line-itoms.service';
import { LineItoms } from '../../../../../../main/webapp/app/entities/line-itoms/line-itoms.model';

describe('Component Tests', () => {

    describe('LineItoms Management Component', () => {
        let comp: LineItomsComponent;
        let fixture: ComponentFixture<LineItomsComponent>;
        let service: LineItomsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BillDetailsTestModule],
                declarations: [LineItomsComponent],
                providers: [
                    LineItomsService
                ]
            })
            .overrideTemplate(LineItomsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LineItomsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LineItomsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new LineItoms(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.lineItoms[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
