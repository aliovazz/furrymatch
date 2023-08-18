import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BreedFormService, BreedFormGroup } from './breed-form.service';
import { IBreed } from '../breed.model';
import { BreedService } from '../service/breed.service';

@Component({
  selector: 'jhi-breed-update',
  templateUrl: './breed-update.component.html',
})
export class BreedUpdateComponent implements OnInit {
  isSaving = false;
  breed: IBreed | null = null;

  editForm: BreedFormGroup = this.breedFormService.createBreedFormGroup();

  constructor(
    protected breedService: BreedService,
    protected breedFormService: BreedFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ breed }) => {
      this.breed = breed;
      if (breed) {
        this.updateForm(breed);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const breed = this.breedFormService.getBreed(this.editForm);
    if (breed.id !== null) {
      this.subscribeToSaveResponse(this.breedService.update(breed));
    } else {
      this.subscribeToSaveResponse(this.breedService.create(breed));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBreed>>): void {
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

  protected updateForm(breed: IBreed): void {
    this.breed = breed;
    this.breedFormService.resetForm(this.editForm, breed);
  }
}
