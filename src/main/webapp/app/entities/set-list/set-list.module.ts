import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SetListComponent } from './list/set-list.component';
import { SetListDetailComponent } from './detail/set-list-detail.component';
import { SetListUpdateComponent } from './update/set-list-update.component';
import { SetListDeleteDialogComponent } from './delete/set-list-delete-dialog.component';
import { SetListRoutingModule } from './route/set-list-routing.module';

@NgModule({
  imports: [SharedModule, SetListRoutingModule],
  declarations: [SetListComponent, SetListDetailComponent, SetListUpdateComponent, SetListDeleteDialogComponent],
  entryComponents: [SetListDeleteDialogComponent],
})
export class SetListModule {}
