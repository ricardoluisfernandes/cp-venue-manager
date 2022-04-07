import { IExpense } from 'app/entities/expense/expense.model';
import { IMember } from 'app/entities/member/member.model';
import { IVenue } from 'app/entities/venue/venue.model';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface IPayment {
  id?: number;
  type?: PaymentType;
  value?: number;
  description?: string | null;
  status?: PaymentStatus;
  expense?: IExpense | null;
  member?: IMember | null;
  venue?: IVenue | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public type?: PaymentType,
    public value?: number,
    public description?: string | null,
    public status?: PaymentStatus,
    public expense?: IExpense | null,
    public member?: IMember | null,
    public venue?: IVenue | null
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
