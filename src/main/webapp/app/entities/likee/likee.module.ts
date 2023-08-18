import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LikeeComponent } from './list/likee.component';
import { LikeeDetailComponent } from './detail/likee-detail.component';
import { LikeeUpdateComponent } from './update/likee-update.component';
import { LikeeDeleteDialogComponent } from './delete/likee-delete-dialog.component';
import { LikeeRoutingModule } from './route/likee-routing.module';

@NgModule({
  imports: [SharedModule, LikeeRoutingModule],
  declarations: [LikeeComponent, LikeeDetailComponent, LikeeUpdateComponent, LikeeDeleteDialogComponent],
})
export class LikeeModule {}
