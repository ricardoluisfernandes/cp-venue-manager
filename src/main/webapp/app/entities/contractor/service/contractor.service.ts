import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContractor, getContractorIdentifier } from '../contractor.model';

export type EntityResponseType = HttpResponse<IContractor>;
export type EntityArrayResponseType = HttpResponse<IContractor[]>;

@Injectable({ providedIn: 'root' })
export class ContractorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contractors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contractor: IContractor): Observable<EntityResponseType> {
    return this.http.post<IContractor>(this.resourceUrl, contractor, { observe: 'response' });
  }

  update(contractor: IContractor): Observable<EntityResponseType> {
    return this.http.put<IContractor>(`${this.resourceUrl}/${getContractorIdentifier(contractor) as number}`, contractor, {
      observe: 'response',
    });
  }

  partialUpdate(contractor: IContractor): Observable<EntityResponseType> {
    return this.http.patch<IContractor>(`${this.resourceUrl}/${getContractorIdentifier(contractor) as number}`, contractor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContractor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContractor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContractorToCollectionIfMissing(
    contractorCollection: IContractor[],
    ...contractorsToCheck: (IContractor | null | undefined)[]
  ): IContractor[] {
    const contractors: IContractor[] = contractorsToCheck.filter(isPresent);
    if (contractors.length > 0) {
      const contractorCollectionIdentifiers = contractorCollection.map(contractorItem => getContractorIdentifier(contractorItem)!);
      const contractorsToAdd = contractors.filter(contractorItem => {
        const contractorIdentifier = getContractorIdentifier(contractorItem);
        if (contractorIdentifier == null || contractorCollectionIdentifiers.includes(contractorIdentifier)) {
          return false;
        }
        contractorCollectionIdentifiers.push(contractorIdentifier);
        return true;
      });
      return [...contractorsToAdd, ...contractorCollection];
    }
    return contractorCollection;
  }
}
