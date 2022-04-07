import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISetListLine } from '../set-list-line.model';
import { SetListLineService } from '../service/set-list-line.service';

@Component({
  templateUrl: './set-list-line-delete-dialog.component.html',
})
export class SetListLineDeleteDialogComponent {
  setListLine?: ISetListLine;

  constructor(protected setListLineService: SetListLineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.setListLineService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
