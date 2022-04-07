import { IMemberAvailability } from 'app/entities/member-availability/member-availability.model';
import { IExpense } from 'app/entities/expense/expense.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { IVenue } from 'app/entities/venue/venue.model';
import { VoiceType } from 'app/entities/enumerations/voice-type.model';

export interface IMember {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string | null;
  voiceType?: VoiceType;
  memberAvailabilities?: IMemberAvailability[] | null;
  expenses?: IExpense[] | null;
  payments?: IPayment[] | null;
  venues?: IVenue[] | null;
}

export class Member implements IMember {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string | null,
    public voiceType?: VoiceType,
    public memberAvailabilities?: IMemberAvailability[] | null,
    public expenses?: IExpense[] | null,
    public payments?: IPayment[] | null,
    public venues?: IVenue[] | null
  ) {}
}

export function getMemberIdentifier(member: IMember): number | undefined {
  return member.id;
}
