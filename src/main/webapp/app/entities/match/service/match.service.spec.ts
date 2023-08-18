import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMatch } from '../match.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../match.test-samples';

import { MatchService, RestMatch } from './match.service';

const requireRestSample: RestMatch = {
  ...sampleWithRequiredData,
  dateMatch: sampleWithRequiredData.dateMatch?.format(DATE_FORMAT),
};

describe('Match Service', () => {
  let service: MatchService;
  let httpMock: HttpTestingController;
  let expectedResult: IMatch | IMatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MatchService);
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

    it('should create a Match', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const match = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(match).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Match', () => {
      const match = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(match).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Match', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Match', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Match', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMatchToCollectionIfMissing', () => {
      it('should add a Match to an empty array', () => {
        const match: IMatch = sampleWithRequiredData;
        expectedResult = service.addMatchToCollectionIfMissing([], match);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(match);
      });

      it('should not add a Match to an array that contains it', () => {
        const match: IMatch = sampleWithRequiredData;
        const matchCollection: IMatch[] = [
          {
            ...match,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMatchToCollectionIfMissing(matchCollection, match);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Match to an array that doesn't contain it", () => {
        const match: IMatch = sampleWithRequiredData;
        const matchCollection: IMatch[] = [sampleWithPartialData];
        expectedResult = service.addMatchToCollectionIfMissing(matchCollection, match);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(match);
      });

      it('should add only unique Match to an array', () => {
        const matchArray: IMatch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const matchCollection: IMatch[] = [sampleWithRequiredData];
        expectedResult = service.addMatchToCollectionIfMissing(matchCollection, ...matchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const match: IMatch = sampleWithRequiredData;
        const match2: IMatch = sampleWithPartialData;
        expectedResult = service.addMatchToCollectionIfMissing([], match, match2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(match);
        expect(expectedResult).toContain(match2);
      });

      it('should accept null and undefined values', () => {
        const match: IMatch = sampleWithRequiredData;
        expectedResult = service.addMatchToCollectionIfMissing([], null, match, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(match);
      });

      it('should return initial array if no Match is added', () => {
        const matchCollection: IMatch[] = [sampleWithRequiredData];
        expectedResult = service.addMatchToCollectionIfMissing(matchCollection, undefined, null);
        expect(expectedResult).toEqual(matchCollection);
      });
    });

    describe('compareMatch', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMatch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMatch(entity1, entity2);
        const compareResult2 = service.compareMatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMatch(entity1, entity2);
        const compareResult2 = service.compareMatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMatch(entity1, entity2);
        const compareResult2 = service.compareMatch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
