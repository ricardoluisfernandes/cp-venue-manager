import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContractor, Contractor } from '../contractor.model';

import { ContractorService } from './contractor.service';

describe('Contractor Service', () => {
  let service: ContractorService;
  let httpMock: HttpTestingController;
  let elemDefault: IContractor;
  let expectedResult: IContractor | IContractor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContractorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      email: 'AAAAAAA',
      phoneNumber: 'AAAAAAA',
      comments: 'AAAAAAA',
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

    it('should create a Contractor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Contractor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contractor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          email: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          comments: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contractor', () => {
      const patchObject = Object.assign(
        {
          comments: 'BBBBBB',
        },
        new Contractor()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contractor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          email: 'BBBBBB',
          phoneNumber: 'BBBBBB',
          comments: 'BBBBBB',
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

    it('should delete a Contractor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContractorToCollectionIfMissing', () => {
      it('should add a Contractor to an empty array', () => {
        const contractor: IContractor = { id: 123 };
        expectedResult = service.addContractorToCollectionIfMissing([], contractor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractor);
      });

      it('should not add a Contractor to an array that contains it', () => {
        const contractor: IContractor = { id: 123 };
        const contractorCollection: IContractor[] = [
          {
            ...contractor,
          },
          { id: 456 },
        ];
        expectedResult = service.addContractorToCollectionIfMissing(contractorCollection, contractor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contractor to an array that doesn't contain it", () => {
        const contractor: IContractor = { id: 123 };
        const contractorCollection: IContractor[] = [{ id: 456 }];
        expectedResult = service.addContractorToCollectionIfMissing(contractorCollection, contractor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractor);
      });

      it('should add only unique Contractor to an array', () => {
        const contractorArray: IContractor[] = [{ id: 123 }, { id: 456 }, { id: 37784 }];
        const contractorCollection: IContractor[] = [{ id: 123 }];
        expectedResult = service.addContractorToCollectionIfMissing(contractorCollection, ...contractorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contractor: IContractor = { id: 123 };
        const contractor2: IContractor = { id: 456 };
        expectedResult = service.addContractorToCollectionIfMissing([], contractor, contractor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contractor);
        expect(expectedResult).toContain(contractor2);
      });

      it('should accept null and undefined values', () => {
        const contractor: IContractor = { id: 123 };
        expectedResult = service.addContractorToCollectionIfMissing([], null, contractor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contractor);
      });

      it('should return initial array if no Contractor is added', () => {
        const contractorCollection: IContractor[] = [{ id: 123 }];
        expectedResult = service.addContractorToCollectionIfMissing(contractorCollection, undefined, null);
        expect(expectedResult).toEqual(contractorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
