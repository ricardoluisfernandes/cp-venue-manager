import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMemberAvailability, getMemberAvailabilityIdentifier } from '../member-availability.model';

export type EntityResponseType = HttpResponse<IMemberAvailability>;
export type EntityArrayResponseType = HttpResponse<IMemberAvailability[]>;

@Injectable({ providedIn: 'root' })
export class MemberAvailabilityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/member-availabilities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(memberAvailability: IMemberAvailability): Observable<EntityResponseType> {
    return this.http.post<IMemberAvailability>(this.resourceUrl, memberAvailability, { observe: 'response' });
  }

  update(memberAvailability: IMemberAvailability): Observable<EntityResponseType> {
    return this.http.put<IMemberAvailability>(
      `${this.resourceUrl}/${getMemberAvailabilityIdentifier(memberAvailability) as number}`,
      memberAvailability,
      { observe: 'response' }
    );
  }

  partialUpdate(memberAvailability: IMemberAvailability): Observable<EntityResponseType> {
    return this.http.patch<IMemberAvailability>(
      `${this.resourceUrl}/${getMemberAvailabilityIdentifier(memberAvailability) as number}`,
      memberAvailability,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMemberAvailability>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMemberAvailability[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMemberAvailabilityToCollectionIfMissing(
    memberAvailabilityCollection: IMemberAvailability[],
    ...memberAvailabilitiesToCheck: (IMemberAvailability | null | undefined)[]
  ): IMemberAvailability[] {
    const memberAvailabilities: IMemberAvailability[] = memberAvailabilitiesToCheck.filter(isPresent);
    if (memberAvailabilities.length > 0) {
      const memberAvailabilityCollectionIdentifiers = memberAvailabilityCollection.map(
        memberAvailabilityItem => getMemberAvailabilityIdentifier(memberAvailabilityItem)!
      );
      const memberAvailabilitiesToAdd = memberAvailabilities.filter(memberAvailabilityItem => {
        const memberAvailabilityIdentifier = getMemberAvailabilityIdentifier(memberAvailabilityItem);
        if (memberAvailabilityIdentifier == null || memberAvailabilityCollectionIdentifiers.includes(memberAvailabilityIdentifier)) {
          return false;
        }
        memberAvailabilityCollectionIdentifiers.push(memberAvailabilityIdentifier);
        return true;
      });
      return [...memberAvailabilitiesToAdd, ...memberAvailabilityCollection];
    }
    return memberAvailabilityCollection;
  }
}
