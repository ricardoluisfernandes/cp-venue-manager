import { IPayment } from 'app/entities/payment/payment.model';
import { IMember } from 'app/entities/member/member.model';
import { IVenue } from 'app/entities/venue/venue.model';
import { ExpenseType } from 'app/entities/enumerations/expense-type.model';
import { ExpenseStatus } from 'app/entities/enumerations/expense-status.model';

export interface IExpense {
  id?: number;
  type?: ExpenseType;
  value?: number;
  status?: ExpenseStatus;
  payment?: IPayment | null;
  member?: IMember | null;
  venue?: IVenue | null;
}

export class Expense implements IExpense {
  constructor(
    public id?: number,
    public type?: ExpenseType,
    public value?: number,
    public status?: ExpenseStatus,
    public payment?: IPayment | null,
    public member?: IMember | null,
    public venue?: IVenue | null
  ) {}
}

export function getExpenseIdentifier(expense: IExpense): number | undefined {
  return expense.id;
}
