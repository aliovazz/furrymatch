import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ChatFormService, ChatFormGroup } from './chat-form.service';
import { IChat } from '../chat.model';
import { ChatService } from '../service/chat.service';
import { IMatch } from 'app/entities/match/match.model';
import { MatchService } from 'app/entities/match/service/match.service';

@Component({
  selector: 'jhi-chat-update',
  templateUrl: './chat-update.component.html',
})
export class ChatUpdateComponent implements OnInit {
  isSaving = false;
  chat: IChat | null = null;

  matchesSharedCollection: IMatch[] = [];

  editForm: ChatFormGroup = this.chatFormService.createChatFormGroup();

  constructor(
    protected chatService: ChatService,
    protected chatFormService: ChatFormService,
    protected matchService: MatchService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMatch = (o1: IMatch | null, o2: IMatch | null): boolean => this.matchService.compareMatch(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chat }) => {
      this.chat = chat;
      if (chat) {
        this.updateForm(chat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chat = this.chatFormService.getChat(this.editForm);
    if (chat.id !== null) {
      this.subscribeToSaveResponse(this.chatService.update(chat));
    } else {
      this.subscribeToSaveResponse(this.chatService.create(chat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(chat: IChat): void {
    this.chat = chat;
    this.chatFormService.resetForm(this.editForm, chat);

    this.matchesSharedCollection = this.matchService.addMatchToCollectionIfMissing<IMatch>(this.matchesSharedCollection, chat.match);
  }

  protected loadRelationshipsOptions(): void {
    this.matchService
      .query()
      .pipe(map((res: HttpResponse<IMatch[]>) => res.body ?? []))
      .pipe(map((matches: IMatch[]) => this.matchService.addMatchToCollectionIfMissing<IMatch>(matches, this.chat?.match)))
      .subscribe((matches: IMatch[]) => (this.matchesSharedCollection = matches));
  }
}
