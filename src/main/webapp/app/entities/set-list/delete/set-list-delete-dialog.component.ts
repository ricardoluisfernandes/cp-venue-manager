import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISetList } from '../set-list.model';
import { SetListService } from '../service/set-list.service';

@Component({
  templateUrl: './set-list-delete-dialog.component.html',
})
export class SetListDeleteDialogComponent {
  setList?: ISetList;

  constructor(protected setListService: SetListService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.setListService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
