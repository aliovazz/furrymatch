import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChatFormService } from './chat-form.service';
import { ChatService } from '../service/chat.service';
import { IChat } from '../chat.model';
import { IMatch } from 'app/entities/match/match.model';
import { MatchService } from 'app/entities/match/service/match.service';

import { ChatUpdateComponent } from './chat-update.component';

describe('Chat Management Update Component', () => {
  let comp: ChatUpdateComponent;
  let fixture: ComponentFixture<ChatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chatFormService: ChatFormService;
  let chatService: ChatService;
  let matchService: MatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChatUpdateComponent],
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
      .overrideTemplate(ChatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chatFormService = TestBed.inject(ChatFormService);
    chatService = TestBed.inject(ChatService);
    matchService = TestBed.inject(MatchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Match query and add missing value', () => {
      const chat: IChat = { id: 456 };
      const match: IMatch = { id: 79202 };
      chat.match = match;

      const matchCollection: IMatch[] = [{ id: 48024 }];
      jest.spyOn(matchService, 'query').mockReturnValue(of(new HttpResponse({ body: matchCollection })));
      const additionalMatches = [match];
      const expectedCollection: IMatch[] = [...additionalMatches, ...matchCollection];
      jest.spyOn(matchService, 'addMatchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      expect(matchService.query).toHaveBeenCalled();
      expect(matchService.addMatchToCollectionIfMissing).toHaveBeenCalledWith(
        matchCollection,
        ...additionalMatches.map(expect.objectContaining)
      );
      expect(comp.matchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const chat: IChat = { id: 456 };
      const match: IMatch = { id: 77410 };
      chat.match = match;

      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      expect(comp.matchesSharedCollection).toContain(match);
      expect(comp.chat).toEqual(chat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChat>>();
      const chat = { id: 123 };
      jest.spyOn(chatFormService, 'getChat').mockReturnValue(chat);
      jest.spyOn(chatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chat }));
      saveSubject.complete();

      // THEN
      expect(chatFormService.getChat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chatService.update).toHaveBeenCalledWith(expect.objectContaining(chat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChat>>();
      const chat = { id: 123 };
      jest.spyOn(chatFormService, 'getChat').mockReturnValue({ id: null });
      jest.spyOn(chatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chat }));
      saveSubject.complete();

      // THEN
      expect(chatFormService.getChat).toHaveBeenCalled();
      expect(chatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChat>>();
      const chat = { id: 123 };
      jest.spyOn(chatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chatService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMatch', () => {
      it('Should forward to matchService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(matchService, 'compareMatch');
        comp.compareMatch(entity, entity2);
        expect(matchService.compareMatch).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
