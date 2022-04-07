import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMemberAvailability } from '../member-availability.model';
import { MemberAvailabilityService } from '../service/member-availability.service';
import { MemberAvailabilityDeleteDialogComponent } from '../delete/member-availability-delete-dialog.component';

@Component({
  selector: 'jhi-member-availability',
  templateUrl: './member-availability.component.html',
})
export class MemberAvailabilityComponent implements OnInit {
  memberAvailabilities?: IMemberAvailability[];
  isLoading = false;

  constructor(protected memberAvailabilityService: MemberAvailabilityService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.memberAvailabilityService.query().subscribe({
      next: (res: HttpResponse<IMemberAvailability[]>) => {
        this.isLoading = false;
        this.memberAvailabilities = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMemberAvailability): number {
    return item.id!;
  }

  delete(memberAvailability: IMemberAvailability): void {
    const modalRef = this.modalService.open(MemberAvailabilityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.memberAvailability = memberAvailability;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
