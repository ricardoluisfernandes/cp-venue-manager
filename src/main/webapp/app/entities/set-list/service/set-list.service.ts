import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISetList, getSetListIdentifier } from '../set-list.model';

export type EntityResponseType = HttpResponse<ISetList>;
export type EntityArrayResponseType = HttpResponse<ISetList[]>;

@Injectable({ providedIn: 'root' })
export class SetListService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/set-lists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(setList: ISetList): Observable<EntityResponseType> {
    return this.http.post<ISetList>(this.resourceUrl, setList, { observe: 'response' });
  }

  update(setList: ISetList): Observable<EntityResponseType> {
    return this.http.put<ISetList>(`${this.resourceUrl}/${getSetListIdentifier(setList) as number}`, setList, { observe: 'response' });
  }

  partialUpdate(setList: ISetList): Observable<EntityResponseType> {
    return this.http.patch<ISetList>(`${this.resourceUrl}/${getSetListIdentifier(setList) as number}`, setList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISetList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISetList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSetListToCollectionIfMissing(setListCollection: ISetList[], ...setListsToCheck: (ISetList | null | undefined)[]): ISetList[] {
    const setLists: ISetList[] = setListsToCheck.filter(isPresent);
    if (setLists.length > 0) {
      const setListCollectionIdentifiers = setListCollection.map(setListItem => getSetListIdentifier(setListItem)!);
      const setListsToAdd = setLists.filter(setListItem => {
        const setListIdentifier = getSetListIdentifier(setListItem);
        if (setListIdentifier == null || setListCollectionIdentifiers.includes(setListIdentifier)) {
          return false;
        }
        setListCollectionIdentifiers.push(setListIdentifier);
        return true;
      });
      return [...setListsToAdd, ...setListCollection];
    }
    return setListCollection;
  }
}
