import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Type } from 'app/entities/enumerations/type.model';
import { VenueStatus } from 'app/entities/enumerations/venue-status.model';
import { IVenue, Venue } from '../venue.model';

import { VenueService } from './venue.service';

describe('Venue Service', () => {
  let service: VenueService;
  let httpMock: HttpTestingController;
  let elemDefault: IVenue;
  let expectedResult: IVenue | IVenue[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VenueService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      instant: currentDate,
      location: 'AAAAAAA',
      distance: 0,
      type: Type.CONCERT,
      status: VenueStatus.DRAFT,
      totalDuration: 'PT1S',
      value: 0,
      considerTravelExpenses: false,
      travelExpensesValue: 0,
      doValueRetention: false,
      retentionPercentage: 0,
      retentionValue: 0,
      memberValue: 0,
      comments: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          instant: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Venue', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          instant: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          instant: currentDate,
        },
        returnedFromService
      );

      service.create(new Venue()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Venue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          instant: currentDate.format(DATE_TIME_FORMAT),
          location: 'BBBBBB',
          distance: 1,
          type: 'BBBBBB',
          status: 'BBBBBB',
          totalDuration: 'BBBBBB',
          value: 1,
          considerTravelExpenses: true,
          travelExpensesValue: 1,
          doValueRetention: true,
          retentionPercentage: 1,
          retentionValue: 1,
          memberValue: 1,
          comments: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          instant: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Venue', () => {
      const patchObject = Object.assign(
        {
          location: 'BBBBBB',
          type: 'BBBBBB',
          totalDuration: 'BBBBBB',
          memberValue: 1,
          comments: 'BBBBBB',
        },
        new Venue()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          instant: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Venue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          instant: currentDate.format(DATE_TIME_FORMAT),
          location: 'BBBBBB',
          distance: 1,
          type: 'BBBBBB',
          status: 'BBBBBB',
          totalDuration: 'BBBBBB',
          value: 1,
          considerTravelExpenses: true,
          travelExpensesValue: 1,
          doValueRetention: true,
          retentionPercentage: 1,
          retentionValue: 1,
          memberValue: 1,
          comments: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          instant: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Venue', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVenueToCollectionIfMissing', () => {
      it('should add a Venue to an empty array', () => {
        const venue: IVenue = { id: 123 };
        expectedResult = service.addVenueToCollectionIfMissing([], venue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(venue);
      });

      it('should not add a Venue to an array that contains it', () => {
        const venue: IVenue = { id: 123 };
        const venueCollection: IVenue[] = [
          {
            ...venue,
          },
          { id: 456 },
        ];
        expectedResult = service.addVenueToCollectionIfMissing(venueCollection, venue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Venue to an array that doesn't contain it", () => {
        const venue: IVenue = { id: 123 };
        const venueCollection: IVenue[] = [{ id: 456 }];
        expectedResult = service.addVenueToCollectionIfMissing(venueCollection, venue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(venue);
      });

      it('should add only unique Venue to an array', () => {
        const venueArray: IVenue[] = [{ id: 123 }, { id: 456 }, { id: 59784 }];
        const venueCollection: IVenue[] = [{ id: 123 }];
        expectedResult = service.addVenueToCollectionIfMissing(venueCollection, ...venueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const venue: IVenue = { id: 123 };
        const venue2: IVenue = { id: 456 };
        expectedResult = service.addVenueToCollectionIfMissing([], venue, venue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(venue);
        expect(expectedResult).toContain(venue2);
      });

      it('should accept null and undefined values', () => {
        const venue: IVenue = { id: 123 };
        expectedResult = service.addVenueToCollectionIfMissing([], null, venue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(venue);
      });

      it('should return initial array if no Venue is added', () => {
        const venueCollection: IVenue[] = [{ id: 123 }];
        expectedResult = service.addVenueToCollectionIfMissing(venueCollection, undefined, null);
        expect(expectedResult).toEqual(venueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
