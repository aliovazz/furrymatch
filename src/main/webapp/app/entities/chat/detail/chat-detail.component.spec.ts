import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChatDetailComponent } from './chat-detail.component';

describe('Chat Management Detail Component', () => {
  let comp: ChatDetailComponent;
  let fixture: ComponentFixture<ChatDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChatDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ chat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ChatDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load chat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.chat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
