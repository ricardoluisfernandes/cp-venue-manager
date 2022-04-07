import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMemberAvailability } from '../member-availability.model';

@Component({
  selector: 'jhi-member-availability-detail',
  templateUrl: './member-availability-detail.component.html',
})
export class MemberAvailabilityDetailComponent implements OnInit {
  memberAvailability: IMemberAvailability | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ memberAvailability }) => {
      this.memberAvailability = memberAvailability;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
