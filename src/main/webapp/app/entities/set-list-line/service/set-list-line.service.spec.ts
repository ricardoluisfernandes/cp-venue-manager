import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISetListLine, SetListLine } from '../set-list-line.model';

import { SetListLineService } from './set-list-line.service';

describe('SetListLine Service', () => {
  let service: SetListLineService;
  let httpMock: HttpTestingController;
  let elemDefault: ISetListLine;
  let expectedResult: ISetListLine | ISetListLine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SetListLineService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      order: 0,
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

    it('should create a SetListLine', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SetListLine()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SetListLine', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          order: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SetListLine', () => {
      const patchObject = Object.assign({}, new SetListLine());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SetListLine', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          order: 1,
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

    it('should delete a SetListLine', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSetListLineToCollectionIfMissing', () => {
      it('should add a SetListLine to an empty array', () => {
        const setListLine: ISetListLine = { id: 123 };
        expectedResult = service.addSetListLineToCollectionIfMissing([], setListLine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(setListLine);
      });

      it('should not add a SetListLine to an array that contains it', () => {
        const setListLine: ISetListLine = { id: 123 };
        const setListLineCollection: ISetListLine[] = [
          {
            ...setListLine,
          },
          { id: 456 },
        ];
        expectedResult = service.addSetListLineToCollectionIfMissing(setListLineCollection, setListLine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SetListLine to an array that doesn't contain it", () => {
        const setListLine: ISetListLine = { id: 123 };
        const setListLineCollection: ISetListLine[] = [{ id: 456 }];
        expectedResult = service.addSetListLineToCollectionIfMissing(setListLineCollection, setListLine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(setListLine);
      });

      it('should add only unique SetListLine to an array', () => {
        const setListLineArray: ISetListLine[] = [{ id: 123 }, { id: 456 }, { id: 1833 }];
        const setListLineCollection: ISetListLine[] = [{ id: 123 }];
        expectedResult = service.addSetListLineToCollectionIfMissing(setListLineCollection, ...setListLineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const setListLine: ISetListLine = { id: 123 };
        const setListLine2: ISetListLine = { id: 456 };
        expectedResult = service.addSetListLineToCollectionIfMissing([], setListLine, setListLine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(setListLine);
        expect(expectedResult).toContain(setListLine2);
      });

      it('should accept null and undefined values', () => {
        const setListLine: ISetListLine = { id: 123 };
        expectedResult = service.addSetListLineToCollectionIfMissing([], null, setListLine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(setListLine);
      });

      it('should return initial array if no SetListLine is added', () => {
        const setListLineCollection: ISetListLine[] = [{ id: 123 }];
        expectedResult = service.addSetListLineToCollectionIfMissing(setListLineCollection, undefined, null);
        expect(expectedResult).toEqual(setListLineCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
