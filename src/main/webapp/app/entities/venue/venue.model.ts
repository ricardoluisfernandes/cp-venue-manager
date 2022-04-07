import dayjs from 'dayjs/esm';
import { IMemberAvailability } from 'app/entities/member-availability/member-availability.model';
import { IExpense } from 'app/entities/expense/expense.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { IContractor } from 'app/entities/contractor/contractor.model';
import { IMember } from 'app/entities/member/member.model';
import { Type } from 'app/entities/enumerations/type.model';
import { VenueStatus } from 'app/entities/enumerations/venue-status.model';

export interface IVenue {
  id?: number;
  instant?: dayjs.Dayjs;
  location?: string;
  distance?: number;
  type?: Type;
  status?: VenueStatus;
  totalDuration?: string | null;
  value?: number | null;
  considerTravelExpenses?: boolean | null;
  travelExpensesValue?: number | null;
  doValueRetention?: boolean | null;
  retentionPercentage?: number | null;
  retentionValue?: number | null;
  memberValue?: number | null;
  comments?: string | null;
  memberAvailabilities?: IMemberAvailability[] | null;
  expenses?: IExpense[] | null;
  payments?: IPayment[] | null;
  contractor?: IContractor | null;
  members?: IMember[] | null;
}

export class Venue implements IVenue {
  constructor(
    public id?: number,
    public instant?: dayjs.Dayjs,
    public location?: string,
    public distance?: number,
    public type?: Type,
    public status?: VenueStatus,
    public totalDuration?: string | null,
    public value?: number | null,
    public considerTravelExpenses?: boolean | null,
    public travelExpensesValue?: number | null,
    public doValueRetention?: boolean | null,
    public retentionPercentage?: number | null,
    public retentionValue?: number | null,
    public memberValue?: number | null,
    public comments?: string | null,
    public memberAvailabilities?: IMemberAvailability[] | null,
    public expenses?: IExpense[] | null,
    public payments?: IPayment[] | null,
    public contractor?: IContractor | null,
    public members?: IMember[] | null
  ) {
    this.considerTravelExpenses = this.considerTravelExpenses ?? false;
    this.doValueRetention = this.doValueRetention ?? false;
  }
}

export function getVenueIdentifier(venue: IVenue): number | undefined {
  return venue.id;
}
