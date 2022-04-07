import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISetListLine, getSetListLineIdentifier } from '../set-list-line.model';

export type EntityResponseType = HttpResponse<ISetListLine>;
export type EntityArrayResponseType = HttpResponse<ISetListLine[]>;

@Injectable({ providedIn: 'root' })
export class SetListLineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/set-list-lines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(setListLine: ISetListLine): Observable<EntityResponseType> {
    return this.http.post<ISetListLine>(this.resourceUrl, setListLine, { observe: 'response' });
  }

  update(setListLine: ISetListLine): Observable<EntityResponseType> {
    return this.http.put<ISetListLine>(`${this.resourceUrl}/${getSetListLineIdentifier(setListLine) as number}`, setListLine, {
      observe: 'response',
    });
  }

  partialUpdate(setListLine: ISetListLine): Observable<EntityResponseType> {
    return this.http.patch<ISetListLine>(`${this.resourceUrl}/${getSetListLineIdentifier(setListLine) as number}`, setListLine, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISetListLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISetListLine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSetListLineToCollectionIfMissing(
    setListLineCollection: ISetListLine[],
    ...setListLinesToCheck: (ISetListLine | null | undefined)[]
  ): ISetListLine[] {
    const setListLines: ISetListLine[] = setListLinesToCheck.filter(isPresent);
    if (setListLines.length > 0) {
      const setListLineCollectionIdentifiers = setListLineCollection.map(setListLineItem => getSetListLineIdentifier(setListLineItem)!);
      const setListLinesToAdd = setListLines.filter(setListLineItem => {
        const setListLineIdentifier = getSetListLineIdentifier(setListLineItem);
        if (setListLineIdentifier == null || setListLineCollectionIdentifiers.includes(setListLineIdentifier)) {
          return false;
        }
        setListLineCollectionIdentifiers.push(setListLineIdentifier);
        return true;
      });
      return [...setListLinesToAdd, ...setListLineCollection];
    }
    return setListLineCollection;
  }
}
