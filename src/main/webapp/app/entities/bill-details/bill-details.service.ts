import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { BillDetails } from './bill-details.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<BillDetails>;

@Injectable()
export class BillDetailsService {

    private resourceUrl =  SERVER_API_URL + 'api/bill-details';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bill-details';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(billDetails: BillDetails): Observable<EntityResponseType> {
        const copy = this.convert(billDetails);
        return this.http.post<BillDetails>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(billDetails: BillDetails): Observable<EntityResponseType> {
        const copy = this.convert(billDetails);
        return this.http.put<BillDetails>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<BillDetails>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<BillDetails[]>> {
        const options = createRequestOption(req);
        return this.http.get<BillDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BillDetails[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<BillDetails[]>> {
        const options = createRequestOption(req);
        return this.http.get<BillDetails[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BillDetails[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: BillDetails = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BillDetails[]>): HttpResponse<BillDetails[]> {
        const jsonResponse: BillDetails[] = res.body;
        const body: BillDetails[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to BillDetails.
     */
    private convertItemFromServer(billDetails: BillDetails): BillDetails {
        const copy: BillDetails = Object.assign({}, billDetails);
        copy.date = this.dateUtils
            .convertDateTimeFromServer(billDetails.date);
        return copy;
    }

    /**
     * Convert a BillDetails to a JSON which can be sent to the server.
     */
    private convert(billDetails: BillDetails): BillDetails {
        const copy: BillDetails = Object.assign({}, billDetails);

        copy.date = this.dateUtils.toDate(billDetails.date);
        return copy;
    }
}
