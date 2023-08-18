import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PhotoFormService, PhotoFormGroup } from './photo-form.service';
import { IPhoto } from '../photo.model';
import { PhotoService } from '../service/photo.service';
import { IPet } from 'app/entities/pet/pet.model';
import { PetService } from 'app/entities/pet/service/pet.service';

@Component({
  selector: 'jhi-photo-update',
  templateUrl: './photo-update.component.html',
})
export class PhotoUpdateComponent implements OnInit {
  isSaving = false;
  photo: IPhoto | null = null;

  petsSharedCollection: IPet[] = [];

  editForm: PhotoFormGroup = this.photoFormService.createPhotoFormGroup();

  constructor(
    protected photoService: PhotoService,
    protected photoFormService: PhotoFormService,
    protected petService: PetService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePet = (o1: IPet | null, o2: IPet | null): boolean => this.petService.comparePet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ photo }) => {
      this.photo = photo;
      if (photo) {
        this.updateForm(photo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const photo = this.photoFormService.getPhoto(this.editForm);
    if (photo.id !== null) {
      this.subscribeToSaveResponse(this.photoService.update(photo));
    } else {
      this.subscribeToSaveResponse(this.photoService.create(photo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhoto>>): void {
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

  protected updateForm(photo: IPhoto): void {
    this.photo = photo;
    this.photoFormService.resetForm(this.editForm, photo);

    this.petsSharedCollection = this.petService.addPetToCollectionIfMissing<IPet>(this.petsSharedCollection, photo.pet);
  }

  protected loadRelationshipsOptions(): void {
    this.petService
      .query()
      .pipe(map((res: HttpResponse<IPet[]>) => res.body ?? []))
      .pipe(map((pets: IPet[]) => this.petService.addPetToCollectionIfMissing<IPet>(pets, this.photo?.pet)))
      .subscribe((pets: IPet[]) => (this.petsSharedCollection = pets));
  }
}
