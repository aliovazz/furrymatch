import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBreed } from '../breed.model';

@Component({
  selector: 'jhi-breed-detail',
  templateUrl: './breed-detail.component.html',
})
export class BreedDetailComponent implements OnInit {
  breed: IBreed | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ breed }) => {
      this.breed = breed;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
