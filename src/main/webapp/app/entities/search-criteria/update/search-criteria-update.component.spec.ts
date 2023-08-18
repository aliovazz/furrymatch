import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SearchCriteriaFormService } from './search-criteria-form.service';
import { SearchCriteriaService } from '../service/search-criteria.service';
import { ISearchCriteria } from '../search-criteria.model';
import { IPet } from 'app/entities/pet/pet.model';
import { PetService } from 'app/entities/pet/service/pet.service';

import { SearchCriteriaUpdateComponent } from './search-criteria-update.component';

describe('SearchCriteria Management Update Component', () => {
  let comp: SearchCriteriaUpdateComponent;
  let fixture: ComponentFixture<SearchCriteriaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let searchCriteriaFormService: SearchCriteriaFormService;
  let searchCriteriaService: SearchCriteriaService;
  let petService: PetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SearchCriteriaUpdateComponent],
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
      .overrideTemplate(SearchCriteriaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SearchCriteriaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    searchCriteriaFormService = TestBed.inject(SearchCriteriaFormService);
    searchCriteriaService = TestBed.inject(SearchCriteriaService);
    petService = TestBed.inject(PetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pet query and add missing value', () => {
      const searchCriteria: ISearchCriteria = { id: 456 };
      const pet: IPet = { id: 84827 };
      searchCriteria.pet = pet;

      const petCollection: IPet[] = [{ id: 66398 }];
      jest.spyOn(petService, 'query').mockReturnValue(of(new HttpResponse({ body: petCollection })));
      const additionalPets = [pet];
      const expectedCollection: IPet[] = [...additionalPets, ...petCollection];
      jest.spyOn(petService, 'addPetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ searchCriteria });
      comp.ngOnInit();

      expect(petService.query).toHaveBeenCalled();
      expect(petService.addPetToCollectionIfMissing).toHaveBeenCalledWith(petCollection, ...additionalPets.map(expect.objectContaining));
      expect(comp.petsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const searchCriteria: ISearchCriteria = { id: 456 };
      const pet: IPet = { id: 80503 };
      searchCriteria.pet = pet;

      activatedRoute.data = of({ searchCriteria });
      comp.ngOnInit();

      expect(comp.petsSharedCollection).toContain(pet);
      expect(comp.searchCriteria).toEqual(searchCriteria);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchCriteria>>();
      const searchCriteria = { id: 123 };
      jest.spyOn(searchCriteriaFormService, 'getSearchCriteria').mockReturnValue(searchCriteria);
      jest.spyOn(searchCriteriaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchCriteria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchCriteria }));
      saveSubject.complete();

      // THEN
      expect(searchCriteriaFormService.getSearchCriteria).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(searchCriteriaService.update).toHaveBeenCalledWith(expect.objectContaining(searchCriteria));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchCriteria>>();
      const searchCriteria = { id: 123 };
      jest.spyOn(searchCriteriaFormService, 'getSearchCriteria').mockReturnValue({ id: null });
      jest.spyOn(searchCriteriaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchCriteria: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchCriteria }));
      saveSubject.complete();

      // THEN
      expect(searchCriteriaFormService.getSearchCriteria).toHaveBeenCalled();
      expect(searchCriteriaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchCriteria>>();
      const searchCriteria = { id: 123 };
      jest.spyOn(searchCriteriaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchCriteria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(searchCriteriaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePet', () => {
      it('Should forward to petService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(petService, 'comparePet');
        comp.comparePet(entity, entity2);
        expect(petService.comparePet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
