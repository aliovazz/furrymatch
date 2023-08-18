import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISearchCriteria } from '../search-criteria.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../search-criteria.test-samples';

import { SearchCriteriaService } from './search-criteria.service';

const requireRestSample: ISearchCriteria = {
  ...sampleWithRequiredData,
};

describe('SearchCriteria Service', () => {
  let service: SearchCriteriaService;
  let httpMock: HttpTestingController;
  let expectedResult: ISearchCriteria | ISearchCriteria[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SearchCriteriaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SearchCriteria', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const searchCriteria = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(searchCriteria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SearchCriteria', () => {
      const searchCriteria = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(searchCriteria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SearchCriteria', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SearchCriteria', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SearchCriteria', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSearchCriteriaToCollectionIfMissing', () => {
      it('should add a SearchCriteria to an empty array', () => {
        const searchCriteria: ISearchCriteria = sampleWithRequiredData;
        expectedResult = service.addSearchCriteriaToCollectionIfMissing([], searchCriteria);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(searchCriteria);
      });

      it('should not add a SearchCriteria to an array that contains it', () => {
        const searchCriteria: ISearchCriteria = sampleWithRequiredData;
        const searchCriteriaCollection: ISearchCriteria[] = [
          {
            ...searchCriteria,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSearchCriteriaToCollectionIfMissing(searchCriteriaCollection, searchCriteria);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SearchCriteria to an array that doesn't contain it", () => {
        const searchCriteria: ISearchCriteria = sampleWithRequiredData;
        const searchCriteriaCollection: ISearchCriteria[] = [sampleWithPartialData];
        expectedResult = service.addSearchCriteriaToCollectionIfMissing(searchCriteriaCollection, searchCriteria);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchCriteria);
      });

      it('should add only unique SearchCriteria to an array', () => {
        const searchCriteriaArray: ISearchCriteria[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const searchCriteriaCollection: ISearchCriteria[] = [sampleWithRequiredData];
        expectedResult = service.addSearchCriteriaToCollectionIfMissing(searchCriteriaCollection, ...searchCriteriaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const searchCriteria: ISearchCriteria = sampleWithRequiredData;
        const searchCriteria2: ISearchCriteria = sampleWithPartialData;
        expectedResult = service.addSearchCriteriaToCollectionIfMissing([], searchCriteria, searchCriteria2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(searchCriteria);
        expect(expectedResult).toContain(searchCriteria2);
      });

      it('should accept null and undefined values', () => {
        const searchCriteria: ISearchCriteria = sampleWithRequiredData;
        expectedResult = service.addSearchCriteriaToCollectionIfMissing([], null, searchCriteria, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(searchCriteria);
      });

      it('should return initial array if no SearchCriteria is added', () => {
        const searchCriteriaCollection: ISearchCriteria[] = [sampleWithRequiredData];
        expectedResult = service.addSearchCriteriaToCollectionIfMissing(searchCriteriaCollection, undefined, null);
        expect(expectedResult).toEqual(searchCriteriaCollection);
      });
    });

    describe('compareSearchCriteria', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSearchCriteria(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSearchCriteria(entity1, entity2);
        const compareResult2 = service.compareSearchCriteria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSearchCriteria(entity1, entity2);
        const compareResult2 = service.compareSearchCriteria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSearchCriteria(entity1, entity2);
        const compareResult2 = service.compareSearchCriteria(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
