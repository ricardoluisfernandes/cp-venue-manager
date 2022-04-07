import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContractor } from '../contractor.model';
import { ContractorService } from '../service/contractor.service';

@Component({
  templateUrl: './contractor-delete-dialog.component.html',
})
export class ContractorDeleteDialogComponent {
  contractor?: IContractor;

  constructor(protected contractorService: ContractorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
