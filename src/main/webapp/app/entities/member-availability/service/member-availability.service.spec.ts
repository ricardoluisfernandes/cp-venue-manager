import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Availability } from 'app/entities/enumerations/availability.model';
import { IMemberAvailability, MemberAvailability } from '../member-availability.model';

import { MemberAvailabilityService } from './member-availability.service';

describe('MemberAvailability Service', () => {
  let service: MemberAvailabilityService;
  let httpMock: HttpTestingController;
  let elemDefault: IMemberAvailability;
  let expectedResult: IMemberAvailability | IMemberAvailability[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MemberAvailabilityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      availability: Availability.AVAILABLE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MemberAvailability', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MemberAvailability()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MemberAvailability', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          availability: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MemberAvailability', () => {
      const patchObject = Object.assign(
        {
          availability: 'BBBBBB',
        },
        new MemberAvailability()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MemberAvailability', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          availability: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a MemberAvailability', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMemberAvailabilityToCollectionIfMissing', () => {
      it('should add a MemberAvailability to an empty array', () => {
        const memberAvailability: IMemberAvailability = { id: 123 };
        expectedResult = service.addMemberAvailabilityToCollectionIfMissing([], memberAvailability);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(memberAvailability);
      });

      it('should not add a MemberAvailability to an array that contains it', () => {
        const memberAvailability: IMemberAvailability = { id: 123 };
        const memberAvailabilityCollection: IMemberAvailability[] = [
          {
            ...memberAvailability,
          },
          { id: 456 },
        ];
        expectedResult = service.addMemberAvailabilityToCollectionIfMissing(memberAvailabilityCollection, memberAvailability);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MemberAvailability to an array that doesn't contain it", () => {
        const memberAvailability: IMemberAvailability = { id: 123 };
        const memberAvailabilityCollection: IMemberAvailability[] = [{ id: 456 }];
        expectedResult = service.addMemberAvailabilityToCollectionIfMissing(memberAvailabilityCollection, memberAvailability);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(memberAvailability);
      });

      it('should add only unique MemberAvailability to an array', () => {
        const memberAvailabilityArray: IMemberAvailability[] = [{ id: 123 }, { id: 456 }, { id: 26601 }];
        const memberAvailabilityCollection: IMemberAvailability[] = [{ id: 123 }];
        expectedResult = service.addMemberAvailabilityToCollectionIfMissing(memberAvailabilityCollection, ...memberAvailabilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const memberAvailability: IMemberAvailability = { id: 123 };
        const memberAvailability2: IMemberAvailability = { id: 456 };
        expectedResult = service.addMemberAvailabilityToCollectionIfMissing([], memberAvailability, memberAvailability2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(memberAvailability);
        expect(expectedResult).toContain(memberAvailability2);
      });

      it('should accept null and undefined values', () => {
        const memberAvailability: IMemberAvailability = { id: 123 };
        expectedResult = service.addMemberAvailabilityToCollectionIfMissing([], null, memberAvailability, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(memberAvailability);
      });

      it('should return initial array if no MemberAvailability is added', () => {
        const memberAvailabilityCollection: IMemberAvailability[] = [{ id: 123 }];
        expectedResult = service.addMemberAvailabilityToCollectionIfMissing(memberAvailabilityCollection, undefined, null);
        expect(expectedResult).toEqual(memberAvailabilityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
