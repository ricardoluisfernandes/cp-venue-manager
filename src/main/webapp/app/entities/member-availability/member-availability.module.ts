import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MemberAvailabilityComponent } from './list/member-availability.component';
import { MemberAvailabilityDetailComponent } from './detail/member-availability-detail.component';
import { MemberAvailabilityUpdateComponent } from './update/member-availability-update.component';
import { MemberAvailabilityDeleteDialogComponent } from './delete/member-availability-delete-dialog.component';
import { MemberAvailabilityRoutingModule } from './route/member-availability-routing.module';

@NgModule({
  imports: [SharedModule, MemberAvailabilityRoutingModule],
  declarations: [
    MemberAvailabilityComponent,
    MemberAvailabilityDetailComponent,
    MemberAvailabilityUpdateComponent,
    MemberAvailabilityDeleteDialogComponent,
  ],
  entryComponents: [MemberAvailabilityDeleteDialogComponent],
})
export class MemberAvailabilityModule {}
