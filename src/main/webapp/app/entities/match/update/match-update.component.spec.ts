import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MatchFormService } from './match-form.service';
import { MatchService } from '../service/match.service';
import { IMatch } from '../match.model';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { ILikee } from 'app/entities/likee/likee.model';
import { LikeeService } from 'app/entities/likee/service/likee.service';

import { MatchUpdateComponent } from './match-update.component';

describe('Match Management Update Component', () => {
  let comp: MatchUpdateComponent;
  let fixture: ComponentFixture<MatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let matchFormService: MatchFormService;
  let matchService: MatchService;
  let contractService: ContractService;
  let likeeService: LikeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MatchUpdateComponent],
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
      .overrideTemplate(MatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    matchFormService = TestBed.inject(MatchFormService);
    matchService = TestBed.inject(MatchService);
    contractService = TestBed.inject(ContractService);
    likeeService = TestBed.inject(LikeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call contract query and add missing value', () => {
      const match: IMatch = { id: 456 };
      const contract: IContract = { id: 65162 };
      match.contract = contract;

      const contractCollection: IContract[] = [{ id: 72775 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const expectedCollection: IContract[] = [contract, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ match });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(contractCollection, contract);
      expect(comp.contractsCollection).toEqual(expectedCollection);
    });

    it('Should call Likee query and add missing value', () => {
      const match: IMatch = { id: 456 };
      const firstLiked: ILikee = { id: 50510 };
      match.firstLiked = firstLiked;
      const secondLiked: ILikee = { id: 63918 };
      match.secondLiked = secondLiked;

      const likeeCollection: ILikee[] = [{ id: 64068 }];
      jest.spyOn(likeeService, 'query').mockReturnValue(of(new HttpResponse({ body: likeeCollection })));
      const additionalLikees = [firstLiked, secondLiked];
      const expectedCollection: ILikee[] = [...additionalLikees, ...likeeCollection];
      jest.spyOn(likeeService, 'addLikeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ match });
      comp.ngOnInit();

      expect(likeeService.query).toHaveBeenCalled();
      expect(likeeService.addLikeeToCollectionIfMissing).toHaveBeenCalledWith(
        likeeCollection,
        ...additionalLikees.map(expect.objectContaining)
      );
      expect(comp.likeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const match: IMatch = { id: 456 };
      const contract: IContract = { id: 6154 };
      match.contract = contract;
      const firstLiked: ILikee = { id: 45285 };
      match.firstLiked = firstLiked;
      const secondLiked: ILikee = { id: 973 };
      match.secondLiked = secondLiked;

      activatedRoute.data = of({ match });
      comp.ngOnInit();

      expect(comp.contractsCollection).toContain(contract);
      expect(comp.likeesSharedCollection).toContain(firstLiked);
      expect(comp.likeesSharedCollection).toContain(secondLiked);
      expect(comp.match).toEqual(match);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatch>>();
      const match = { id: 123 };
      jest.spyOn(matchFormService, 'getMatch').mockReturnValue(match);
      jest.spyOn(matchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ match });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: match }));
      saveSubject.complete();

      // THEN
      expect(matchFormService.getMatch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(matchService.update).toHaveBeenCalledWith(expect.objectContaining(match));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatch>>();
      const match = { id: 123 };
      jest.spyOn(matchFormService, 'getMatch').mockReturnValue({ id: null });
      jest.spyOn(matchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ match: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: match }));
      saveSubject.complete();

      // THEN
      expect(matchFormService.getMatch).toHaveBeenCalled();
      expect(matchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatch>>();
      const match = { id: 123 };
      jest.spyOn(matchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ match });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(matchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('Should forward to contractService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLikee', () => {
      it('Should forward to likeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(likeeService, 'compareLikee');
        comp.compareLikee(entity, entity2);
        expect(likeeService.compareLikee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
