import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISetList, SetList } from '../set-list.model';

import { SetListService } from './set-list.service';

describe('SetList Service', () => {
  let service: SetListService;
  let httpMock: HttpTestingController;
  let elemDefault: ISetList;
  let expectedResult: ISetList | ISetList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SetListService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      totalDuration: 'PT1S',
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

    it('should create a SetList', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SetList()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SetList', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          totalDuration: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SetList', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
        },
        new SetList()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SetList', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          totalDuration: 'BBBBBB',
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

    it('should delete a SetList', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSetListToCollectionIfMissing', () => {
      it('should add a SetList to an empty array', () => {
        const setList: ISetList = { id: 123 };
        expectedResult = service.addSetListToCollectionIfMissing([], setList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(setList);
      });

      it('should not add a SetList to an array that contains it', () => {
        const setList: ISetList = { id: 123 };
        const setListCollection: ISetList[] = [
          {
            ...setList,
          },
          { id: 456 },
        ];
        expectedResult = service.addSetListToCollectionIfMissing(setListCollection, setList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SetList to an array that doesn't contain it", () => {
        const setList: ISetList = { id: 123 };
        const setListCollection: ISetList[] = [{ id: 456 }];
        expectedResult = service.addSetListToCollectionIfMissing(setListCollection, setList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(setList);
      });

      it('should add only unique SetList to an array', () => {
        const setListArray: ISetList[] = [{ id: 123 }, { id: 456 }, { id: 60270 }];
        const setListCollection: ISetList[] = [{ id: 123 }];
        expectedResult = service.addSetListToCollectionIfMissing(setListCollection, ...setListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const setList: ISetList = { id: 123 };
        const setList2: ISetList = { id: 456 };
        expectedResult = service.addSetListToCollectionIfMissing([], setList, setList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(setList);
        expect(expectedResult).toContain(setList2);
      });

      it('should accept null and undefined values', () => {
        const setList: ISetList = { id: 123 };
        expectedResult = service.addSetListToCollectionIfMissing([], null, setList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(setList);
      });

      it('should return initial array if no SetList is added', () => {
        const setListCollection: ISetList[] = [{ id: 123 }];
        expectedResult = service.addSetListToCollectionIfMissing(setListCollection, undefined, null);
        expect(expectedResult).toEqual(setListCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
