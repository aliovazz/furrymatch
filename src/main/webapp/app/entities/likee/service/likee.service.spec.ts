import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILikee } from '../likee.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../likee.test-samples';

import { LikeeService } from './likee.service';

const requireRestSample: ILikee = {
  ...sampleWithRequiredData,
};

describe('Likee Service', () => {
  let service: LikeeService;
  let httpMock: HttpTestingController;
  let expectedResult: ILikee | ILikee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LikeeService);
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

    it('should create a Likee', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const likee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(likee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Likee', () => {
      const likee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(likee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Likee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Likee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Likee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLikeeToCollectionIfMissing', () => {
      it('should add a Likee to an empty array', () => {
        const likee: ILikee = sampleWithRequiredData;
        expectedResult = service.addLikeeToCollectionIfMissing([], likee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(likee);
      });

      it('should not add a Likee to an array that contains it', () => {
        const likee: ILikee = sampleWithRequiredData;
        const likeeCollection: ILikee[] = [
          {
            ...likee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLikeeToCollectionIfMissing(likeeCollection, likee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Likee to an array that doesn't contain it", () => {
        const likee: ILikee = sampleWithRequiredData;
        const likeeCollection: ILikee[] = [sampleWithPartialData];
        expectedResult = service.addLikeeToCollectionIfMissing(likeeCollection, likee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(likee);
      });

      it('should add only unique Likee to an array', () => {
        const likeeArray: ILikee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const likeeCollection: ILikee[] = [sampleWithRequiredData];
        expectedResult = service.addLikeeToCollectionIfMissing(likeeCollection, ...likeeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const likee: ILikee = sampleWithRequiredData;
        const likee2: ILikee = sampleWithPartialData;
        expectedResult = service.addLikeeToCollectionIfMissing([], likee, likee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(likee);
        expect(expectedResult).toContain(likee2);
      });

      it('should accept null and undefined values', () => {
        const likee: ILikee = sampleWithRequiredData;
        expectedResult = service.addLikeeToCollectionIfMissing([], null, likee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(likee);
      });

      it('should return initial array if no Likee is added', () => {
        const likeeCollection: ILikee[] = [sampleWithRequiredData];
        expectedResult = service.addLikeeToCollectionIfMissing(likeeCollection, undefined, null);
        expect(expectedResult).toEqual(likeeCollection);
      });
    });

    describe('compareLikee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLikee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLikee(entity1, entity2);
        const compareResult2 = service.compareLikee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLikee(entity1, entity2);
        const compareResult2 = service.compareLikee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLikee(entity1, entity2);
        const compareResult2 = service.compareLikee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
