import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LikeeFormService } from './likee-form.service';
import { LikeeService } from '../service/likee.service';
import { ILikee } from '../likee.model';
import { IPet } from 'app/entities/pet/pet.model';
import { PetService } from 'app/entities/pet/service/pet.service';

import { LikeeUpdateComponent } from './likee-update.component';

describe('Likee Management Update Component', () => {
  let comp: LikeeUpdateComponent;
  let fixture: ComponentFixture<LikeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let likeeFormService: LikeeFormService;
  let likeeService: LikeeService;
  let petService: PetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LikeeUpdateComponent],
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
      .overrideTemplate(LikeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LikeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    likeeFormService = TestBed.inject(LikeeFormService);
    likeeService = TestBed.inject(LikeeService);
    petService = TestBed.inject(PetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pet query and add missing value', () => {
      const likee: ILikee = { id: 456 };
      const firstPet: IPet = { id: 80514 };
      likee.firstPet = firstPet;
      const secondPet: IPet = { id: 33917 };
      likee.secondPet = secondPet;

      const petCollection: IPet[] = [{ id: 16945 }];
      jest.spyOn(petService, 'query').mockReturnValue(of(new HttpResponse({ body: petCollection })));
      const additionalPets = [firstPet, secondPet];
      const expectedCollection: IPet[] = [...additionalPets, ...petCollection];
      jest.spyOn(petService, 'addPetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likee });
      comp.ngOnInit();

      expect(petService.query).toHaveBeenCalled();
      expect(petService.addPetToCollectionIfMissing).toHaveBeenCalledWith(petCollection, ...additionalPets.map(expect.objectContaining));
      expect(comp.petsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const likee: ILikee = { id: 456 };
      const firstPet: IPet = { id: 31882 };
      likee.firstPet = firstPet;
      const secondPet: IPet = { id: 2583 };
      likee.secondPet = secondPet;

      activatedRoute.data = of({ likee });
      comp.ngOnInit();

      expect(comp.petsSharedCollection).toContain(firstPet);
      expect(comp.petsSharedCollection).toContain(secondPet);
      expect(comp.likee).toEqual(likee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikee>>();
      const likee = { id: 123 };
      jest.spyOn(likeeFormService, 'getLikee').mockReturnValue(likee);
      jest.spyOn(likeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likee }));
      saveSubject.complete();

      // THEN
      expect(likeeFormService.getLikee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(likeeService.update).toHaveBeenCalledWith(expect.objectContaining(likee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikee>>();
      const likee = { id: 123 };
      jest.spyOn(likeeFormService, 'getLikee').mockReturnValue({ id: null });
      jest.spyOn(likeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likee }));
      saveSubject.complete();

      // THEN
      expect(likeeFormService.getLikee).toHaveBeenCalled();
      expect(likeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikee>>();
      const likee = { id: 123 };
      jest.spyOn(likeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(likeeService.update).toHaveBeenCalled();
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
