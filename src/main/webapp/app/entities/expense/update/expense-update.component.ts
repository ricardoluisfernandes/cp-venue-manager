import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IExpense, Expense } from '../expense.model';
import { ExpenseService } from '../service/expense.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IVenue } from 'app/entities/venue/venue.model';
import { VenueService } from 'app/entities/venue/service/venue.service';
import { ExpenseType } from 'app/entities/enumerations/expense-type.model';
import { ExpenseStatus } from 'app/entities/enumerations/expense-status.model';

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html',
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  expenseTypeValues = Object.keys(ExpenseType);
  expenseStatusValues = Object.keys(ExpenseStatus);

  paymentsCollection: IPayment[] = [];
  membersSharedCollection: IMember[] = [];
  venuesSharedCollection: IVenue[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    value: [null, [Validators.required, Validators.min(0)]],
    status: [null, [Validators.required]],
    payment: [],
    member: [],
    venue: [],
  });

  constructor(
    protected expenseService: ExpenseService,
    protected paymentService: PaymentService,
    protected memberService: MemberService,
    protected venueService: VenueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.updateForm(expense);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.createFromForm();
    if (expense.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  trackPaymentById(_index: number, item: IPayment): number {
    return item.id!;
  }

  trackMemberById(_index: number, item: IMember): number {
    return item.id!;
  }

  trackVenueById(_index: number, item: IVenue): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
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

  protected updateForm(expense: IExpense): void {
    this.editForm.patchValue({
      id: expense.id,
      type: expense.type,
      value: expense.value,
      status: expense.status,
      payment: expense.payment,
      member: expense.member,
      venue: expense.venue,
    });

    this.paymentsCollection = this.paymentService.addPaymentToCollectionIfMissing(this.paymentsCollection, expense.payment);
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing(this.membersSharedCollection, expense.member);
    this.venuesSharedCollection = this.venueService.addVenueToCollectionIfMissing(this.venuesSharedCollection, expense.venue);
  }

  protected loadRelationshipsOptions(): void {
    this.paymentService
      .query({ filter: 'expense-is-null' })
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(
        map((payments: IPayment[]) => this.paymentService.addPaymentToCollectionIfMissing(payments, this.editForm.get('payment')!.value))
      )
      .subscribe((payments: IPayment[]) => (this.paymentsCollection = payments));

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

  protected createFromForm(): IExpense {
    return {
      ...new Expense(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      value: this.editForm.get(['value'])!.value,
      status: this.editForm.get(['status'])!.value,
      payment: this.editForm.get(['payment'])!.value,
      member: this.editForm.get(['member'])!.value,
      venue: this.editForm.get(['venue'])!.value,
    };
  }
}
