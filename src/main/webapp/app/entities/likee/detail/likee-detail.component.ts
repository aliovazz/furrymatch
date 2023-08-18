import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILikee } from '../likee.model';

@Component({
  selector: 'jhi-likee-detail',
  templateUrl: './likee-detail.component.html',
})
export class LikeeDetailComponent implements OnInit {
  likee: ILikee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ likee }) => {
      this.likee = likee;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
