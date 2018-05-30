import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { LineItoms } from './line-itoms.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<LineItoms>;

@Injectable()
export class LineItomsService {

    private resourceUrl =  SERVER_API_URL + 'api/line-itoms';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/line-itoms';

    constructor(private http: HttpClient) { }

    create(lineItoms: LineItoms): Observable<EntityResponseType> {
        const copy = this.convert(lineItoms);
        return this.http.post<LineItoms>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(lineItoms: LineItoms): Observable<EntityResponseType> {
        const copy = this.convert(lineItoms);
        return this.http.put<LineItoms>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<LineItoms>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<LineItoms[]>> {
        const options = createRequestOption(req);
        return this.http.get<LineItoms[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LineItoms[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<LineItoms[]>> {
        const options = createRequestOption(req);
        return this.http.get<LineItoms[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LineItoms[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: LineItoms = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<LineItoms[]>): HttpResponse<LineItoms[]> {
        const jsonResponse: LineItoms[] = res.body;
        const body: LineItoms[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to LineItoms.
     */
    private convertItemFromServer(lineItoms: LineItoms): LineItoms {
        const copy: LineItoms = Object.assign({}, lineItoms);
        return copy;
    }

    /**
     * Convert a LineItoms to a JSON which can be sent to the server.
     */
    private convert(lineItoms: LineItoms): LineItoms {
        const copy: LineItoms = Object.assign({}, lineItoms);
        return copy;
    }
}
