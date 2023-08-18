import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BreedComponent } from './list/breed.component';
import { BreedDetailComponent } from './detail/breed-detail.component';
import { BreedUpdateComponent } from './update/breed-update.component';
import { BreedDeleteDialogComponent } from './delete/breed-delete-dialog.component';
import { BreedRoutingModule } from './route/breed-routing.module';

@NgModule({
  imports: [SharedModule, BreedRoutingModule],
  declarations: [BreedComponent, BreedDetailComponent, BreedUpdateComponent, BreedDeleteDialogComponent],
})
export class BreedModule {}
