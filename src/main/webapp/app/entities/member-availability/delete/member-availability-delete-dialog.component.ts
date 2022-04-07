import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMemberAvailability } from '../member-availability.model';
import { MemberAvailabilityService } from '../service/member-availability.service';

@Component({
  templateUrl: './member-availability-delete-dialog.component.html',
})
export class MemberAvailabilityDeleteDialogComponent {
  memberAvailability?: IMemberAvailability;

  constructor(protected memberAvailabilityService: MemberAvailabilityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.memberAvailabilityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
