import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SetListLineComponent } from './list/set-list-line.component';
import { SetListLineDetailComponent } from './detail/set-list-line-detail.component';
import { SetListLineUpdateComponent } from './update/set-list-line-update.component';
import { SetListLineDeleteDialogComponent } from './delete/set-list-line-delete-dialog.component';
import { SetListLineRoutingModule } from './route/set-list-line-routing.module';

@NgModule({
  imports: [SharedModule, SetListLineRoutingModule],
  declarations: [SetListLineComponent, SetListLineDetailComponent, SetListLineUpdateComponent, SetListLineDeleteDialogComponent],
  entryComponents: [SetListLineDeleteDialogComponent],
})
export class SetListLineModule {}
