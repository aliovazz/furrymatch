import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SearchCriteriaComponent } from './list/search-criteria.component';
import { SearchCriteriaDetailComponent } from './detail/search-criteria-detail.component';
import { SearchCriteriaUpdateComponent } from './update/search-criteria-update.component';
import { SearchCriteriaDeleteDialogComponent } from './delete/search-criteria-delete-dialog.component';
import { SearchCriteriaRoutingModule } from './route/search-criteria-routing.module';

@NgModule({
  imports: [SharedModule, SearchCriteriaRoutingModule],
  declarations: [
    SearchCriteriaComponent,
    SearchCriteriaDetailComponent,
    SearchCriteriaUpdateComponent,
    SearchCriteriaDeleteDialogComponent,
  ],
})
export class SearchCriteriaModule {}
