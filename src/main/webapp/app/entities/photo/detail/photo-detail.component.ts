import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPhoto } from '../photo.model';

@Component({
  selector: 'jhi-photo-detail',
  templateUrl: './photo-detail.component.html',
})
export class PhotoDetailComponent implements OnInit {
  photo: IPhoto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ photo }) => {
      this.photo = photo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
