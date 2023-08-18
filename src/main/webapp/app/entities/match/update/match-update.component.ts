import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MatchFormService, MatchFormGroup } from './match-form.service';
import { IMatch } from '../match.model';
import { MatchService } from '../service/match.service';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { ILikee } from 'app/entities/likee/likee.model';
import { LikeeService } from 'app/entities/likee/service/likee.service';

@Component({
  selector: 'jhi-match-update',
  templateUrl: './match-update.component.html',
})
export class MatchUpdateComponent implements OnInit {
  isSaving = false;
  match: IMatch | null = null;

  contractsCollection: IContract[] = [];
  likeesSharedCollection: ILikee[] = [];

  editForm: MatchFormGroup = this.matchFormService.createMatchFormGroup();

  constructor(
    protected matchService: MatchService,
    protected matchFormService: MatchFormService,
    protected contractService: ContractService,
    protected likeeService: LikeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  compareLikee = (o1: ILikee | null, o2: ILikee | null): boolean => this.likeeService.compareLikee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ match }) => {
      this.match = match;
      if (match) {
        this.updateForm(match);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const match = this.matchFormService.getMatch(this.editForm);
    if (match.id !== null) {
      this.subscribeToSaveResponse(this.matchService.update(match));
    } else {
      this.subscribeToSaveResponse(this.matchService.create(match));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatch>>): void {
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

  protected updateForm(match: IMatch): void {
    this.match = match;
    this.matchFormService.resetForm(this.editForm, match);

    this.contractsCollection = this.contractService.addContractToCollectionIfMissing<IContract>(this.contractsCollection, match.contract);
    this.likeesSharedCollection = this.likeeService.addLikeeToCollectionIfMissing<ILikee>(
      this.likeesSharedCollection,
      match.firstLiked,
      match.secondLiked
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query({ filter: 'match-is-null' })
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) => this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.match?.contract))
      )
      .subscribe((contracts: IContract[]) => (this.contractsCollection = contracts));

    this.likeeService
      .query()
      .pipe(map((res: HttpResponse<ILikee[]>) => res.body ?? []))
      .pipe(
        map((likees: ILikee[]) =>
          this.likeeService.addLikeeToCollectionIfMissing<ILikee>(likees, this.match?.firstLiked, this.match?.secondLiked)
        )
      )
      .subscribe((likees: ILikee[]) => (this.likeesSharedCollection = likees));
  }
}
