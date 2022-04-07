import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVenue, getVenueIdentifier } from '../venue.model';

export type EntityResponseType = HttpResponse<IVenue>;
export type EntityArrayResponseType = HttpResponse<IVenue[]>;

@Injectable({ providedIn: 'root' })
export class VenueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/venues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(venue: IVenue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(venue);
    return this.http
      .post<IVenue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(venue: IVenue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(venue);
    return this.http
      .put<IVenue>(`${this.resourceUrl}/${getVenueIdentifier(venue) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(venue: IVenue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(venue);
    return this.http
      .patch<IVenue>(`${this.resourceUrl}/${getVenueIdentifier(venue) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVenue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVenue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVenueToCollectionIfMissing(venueCollection: IVenue[], ...venuesToCheck: (IVenue | null | undefined)[]): IVenue[] {
    const venues: IVenue[] = venuesToCheck.filter(isPresent);
    if (venues.length > 0) {
      const venueCollectionIdentifiers = venueCollection.map(venueItem => getVenueIdentifier(venueItem)!);
      const venuesToAdd = venues.filter(venueItem => {
        const venueIdentifier = getVenueIdentifier(venueItem);
        if (venueIdentifier == null || venueCollectionIdentifiers.includes(venueIdentifier)) {
          return false;
        }
        venueCollectionIdentifiers.push(venueIdentifier);
        return true;
      });
      return [...venuesToAdd, ...venueCollection];
    }
    return venueCollection;
  }

  protected convertDateFromClient(venue: IVenue): IVenue {
    return Object.assign({}, venue, {
      instant: venue.instant?.isValid() ? venue.instant.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.instant = res.body.instant ? dayjs(res.body.instant) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((venue: IVenue) => {
        venue.instant = venue.instant ? dayjs(venue.instant) : undefined;
      });
    }
    return res;
  }
}
