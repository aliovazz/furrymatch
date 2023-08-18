import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LikeeFormService, LikeeFormGroup } from './likee-form.service';
import { ILikee } from '../likee.model';
import { LikeeService } from '../service/likee.service';
import { IPet } from 'app/entities/pet/pet.model';
import { PetService } from 'app/entities/pet/service/pet.service';
import { LikeType } from 'app/entities/enumerations/like-type.model';

@Component({
  selector: 'jhi-likee-update',
  templateUrl: './likee-update.component.html',
})
export class LikeeUpdateComponent implements OnInit {
  isSaving = false;
  likee: ILikee | null = null;
  likeTypeValues = Object.keys(LikeType);

  petsSharedCollection: IPet[] = [];

  editForm: LikeeFormGroup = this.likeeFormService.createLikeeFormGroup();

  constructor(
    protected likeeService: LikeeService,
    protected likeeFormService: LikeeFormService,
    protected petService: PetService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePet = (o1: IPet | null, o2: IPet | null): boolean => this.petService.comparePet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ likee }) => {
      this.likee = likee;
      if (likee) {
        this.updateForm(likee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const likee = this.likeeFormService.getLikee(this.editForm);
    if (likee.id !== null) {
      this.subscribeToSaveResponse(this.likeeService.update(likee));
    } else {
      this.subscribeToSaveResponse(this.likeeService.create(likee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILikee>>): void {
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

  protected updateForm(likee: ILikee): void {
    this.likee = likee;
    this.likeeFormService.resetForm(this.editForm, likee);

    this.petsSharedCollection = this.petService.addPetToCollectionIfMissing<IPet>(
      this.petsSharedCollection,
      likee.firstPet,
      likee.secondPet
    );
  }

  protected loadRelationshipsOptions(): void {
    this.petService
      .query()
      .pipe(map((res: HttpResponse<IPet[]>) => res.body ?? []))
      .pipe(map((pets: IPet[]) => this.petService.addPetToCollectionIfMissing<IPet>(pets, this.likee?.firstPet, this.likee?.secondPet)))
      .subscribe((pets: IPet[]) => (this.petsSharedCollection = pets));
  }
}
