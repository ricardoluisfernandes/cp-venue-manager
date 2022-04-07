import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVenue } from '../venue.model';
import { VenueService } from '../service/venue.service';

@Component({
  templateUrl: './venue-delete-dialog.component.html',
})
export class VenueDeleteDialogComponent {
  venue?: IVenue;

  constructor(protected venueService: VenueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.venueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
