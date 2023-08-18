import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISearchCriteria } from '../search-criteria.model';

@Component({
  selector: 'jhi-search-criteria-detail',
  templateUrl: './search-criteria-detail.component.html',
})
export class SearchCriteriaDetailComponent implements OnInit {
  searchCriteria: ISearchCriteria | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ searchCriteria }) => {
      this.searchCriteria = searchCriteria;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
