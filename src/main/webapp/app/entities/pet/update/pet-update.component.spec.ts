import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PetFormService } from './pet-form.service';
import { PetService } from '../service/pet.service';
import { IPet } from '../pet.model';
import { IOwner } from 'app/entities/owner/owner.model';
import { OwnerService } from 'app/entities/owner/service/owner.service';
import { IBreed } from 'app/entities/breed/breed.model';
import { BreedService } from 'app/entities/breed/service/breed.service';

import { PetUpdateComponent } from './pet-update.component';

describe('Pet Management Update Component', () => {
  let comp: PetUpdateComponent;
  let fixture: ComponentFixture<PetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let petFormService: PetFormService;
  let petService: PetService;
  let ownerService: OwnerService;
  let breedService: BreedService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PetUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    petFormService = TestBed.inject(PetFormService);
    petService = TestBed.inject(PetService);
    ownerService = TestBed.inject(OwnerService);
    breedService = TestBed.inject(BreedService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Owner query and add missing value', () => {
      const pet: IPet = { id: 456 };
      const owner: IOwner = { user_id: 79982 };
      pet.owner = owner;

      const ownerCollection: IOwner[] = [{ user_id: 14362 }];
      jest.spyOn(ownerService, 'query').mockReturnValue(of(new HttpResponse({ body: ownerCollection })));
      const additionalOwners = [owner];
      const expectedCollection: IOwner[] = [...additionalOwners, ...ownerCollection];
      jest.spyOn(ownerService, 'addOwnerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      expect(ownerService.query).toHaveBeenCalled();
      expect(ownerService.addOwnerToCollectionIfMissing).toHaveBeenCalledWith(
        ownerCollection,
        ...additionalOwners.map(expect.objectContaining)
      );
      expect(comp.ownersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Breed query and add missing value', () => {
      const pet: IPet = { id: 456 };
      const breed: IBreed = { id: 59294 };
      pet.breed = breed;

      const breedCollection: IBreed[] = [{ id: 8364 }];
      jest.spyOn(breedService, 'query').mockReturnValue(of(new HttpResponse({ body: breedCollection })));
      const additionalBreeds = [breed];
      const expectedCollection: IBreed[] = [...additionalBreeds, ...breedCollection];
      jest.spyOn(breedService, 'addBreedToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      expect(breedService.query).toHaveBeenCalled();
      expect(breedService.addBreedToCollectionIfMissing).toHaveBeenCalledWith(
        breedCollection,
        ...additionalBreeds.map(expect.objectContaining)
      );
      expect(comp.breedsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pet: IPet = { id: 456 };
      const owner: IOwner = { user_id: 68173 };
      pet.owner = owner;
      const breed: IBreed = { id: 49044 };
      pet.breed = breed;

      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      expect(comp.ownersSharedCollection).toContain(owner);
      expect(comp.breedsSharedCollection).toContain(breed);
      expect(comp.pet).toEqual(pet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPet>>();
      const pet = { id: 123 };
      jest.spyOn(petFormService, 'getPet').mockReturnValue(pet);
      jest.spyOn(petService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pet }));
      saveSubject.complete();

      // THEN
      expect(petFormService.getPet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(petService.update).toHaveBeenCalledWith(expect.objectContaining(pet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPet>>();
      const pet = { id: 123 };
      jest.spyOn(petFormService, 'getPet').mockReturnValue({ id: null });
      jest.spyOn(petService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pet }));
      saveSubject.complete();

      // THEN
      expect(petFormService.getPet).toHaveBeenCalled();
      expect(petService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPet>>();
      const pet = { id: 123 };
      jest.spyOn(petService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(petService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOwner', () => {
      it('Should forward to ownerService', () => {
        const entity = { user_id: 123 };
        const entity2 = { user_id: 456 };
        jest.spyOn(ownerService, 'compareOwner');
        comp.compareOwner(entity, entity2);
        expect(ownerService.compareOwner).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBreed', () => {
      it('Should forward to breedService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(breedService, 'compareBreed');
        comp.compareBreed(entity, entity2);
        expect(breedService.compareBreed).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
