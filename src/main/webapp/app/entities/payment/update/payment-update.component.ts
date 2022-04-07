import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPayment, Payment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  paymentTypeValues = Object.keys(PaymentType);
  paymentStatusValues = Object.keys(PaymentStatus);

  membersSharedCollection: IMember[] = [];
  venuesSharedCollection: IVenue[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    value: [null, [Validators.required]],
    description: [],
    status: [null, [Validators.required]],
    member: [],
    venue: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected memberService: MemberService,
    protected venueService: VenueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  trackMemberById(_index: number, item: IMember): number {
    return item.id!;
  }

  trackVenueById(_index: number, item: IVenue): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      type: payment.type,
      value: payment.value,
      description: payment.description,
      status: payment.status,
      member: payment.member,
      venue: payment.venue,
    });

    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing(this.membersSharedCollection, payment.member);
    this.venuesSharedCollection = this.venueService.addVenueToCollectionIfMissing(this.venuesSharedCollection, payment.venue);
  }

  protected loadRelationshipsOptions(): void {
    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing(members, this.editForm.get('member')!.value)))
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));

    this.venueService
      .query()
      .pipe(map((res: HttpResponse<IVenue[]>) => res.body ?? []))
      .pipe(map((venues: IVenue[]) => this.venueService.addVenueToCollectionIfMissing(venues, this.editForm.get('venue')!.value)))
      .subscribe((venues: IVenue[]) => (this.venuesSharedCollection = venues));
  }

  protected createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      value: this.editForm.get(['value'])!.value,
      description: this.editForm.get(['description'])!.value,
      status: this.editForm.get(['status'])!.value,
      member: this.editForm.get(['member'])!.value,
      venue: this.editForm.get(['venue'])!.value,
    };
  }
}
