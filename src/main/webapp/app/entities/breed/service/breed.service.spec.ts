import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBreed } from '../breed.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../breed.test-samples';

import { BreedService } from './breed.service';

const requireRestSample: IBreed = {
  ...sampleWithRequiredData,
};

describe('Breed Service', () => {
  let service: BreedService;
  let httpMock: HttpTestingController;
  let expectedResult: IBreed | IBreed[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BreedService);
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

    it('should create a Breed', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const breed = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(breed).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Breed', () => {
      const breed = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(breed).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Breed', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Breed', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Breed', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBreedToCollectionIfMissing', () => {
      it('should add a Breed to an empty array', () => {
        const breed: IBreed = sampleWithRequiredData;
        expectedResult = service.addBreedToCollectionIfMissing([], breed);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(breed);
      });

      it('should not add a Breed to an array that contains it', () => {
        const breed: IBreed = sampleWithRequiredData;
        const breedCollection: IBreed[] = [
          {
            ...breed,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBreedToCollectionIfMissing(breedCollection, breed);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Breed to an array that doesn't contain it", () => {
        const breed: IBreed = sampleWithRequiredData;
        const breedCollection: IBreed[] = [sampleWithPartialData];
        expectedResult = service.addBreedToCollectionIfMissing(breedCollection, breed);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(breed);
      });

      it('should add only unique Breed to an array', () => {
        const breedArray: IBreed[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const breedCollection: IBreed[] = [sampleWithRequiredData];
        expectedResult = service.addBreedToCollectionIfMissing(breedCollection, ...breedArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const breed: IBreed = sampleWithRequiredData;
        const breed2: IBreed = sampleWithPartialData;
        expectedResult = service.addBreedToCollectionIfMissing([], breed, breed2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(breed);
        expect(expectedResult).toContain(breed2);
      });

      it('should accept null and undefined values', () => {
        const breed: IBreed = sampleWithRequiredData;
        expectedResult = service.addBreedToCollectionIfMissing([], null, breed, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(breed);
      });

      it('should return initial array if no Breed is added', () => {
        const breedCollection: IBreed[] = [sampleWithRequiredData];
        expectedResult = service.addBreedToCollectionIfMissing(breedCollection, undefined, null);
        expect(expectedResult).toEqual(breedCollection);
      });
    });

    describe('compareBreed', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBreed(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBreed(entity1, entity2);
        const compareResult2 = service.compareBreed(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBreed(entity1, entity2);
        const compareResult2 = service.compareBreed(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBreed(entity1, entity2);
        const compareResult2 = service.compareBreed(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
