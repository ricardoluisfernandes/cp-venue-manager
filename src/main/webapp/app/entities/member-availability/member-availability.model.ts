import { IMember } from 'app/entities/member/member.model';
import { IVenue } from 'app/entities/venue/venue.model';
import { Availability } from 'app/entities/enumerations/availability.model';

export interface IMemberAvailability {
  id?: number;
  availability?: Availability;
  member?: IMember | null;
  venue?: IVenue | null;
}

export class MemberAvailability implements IMemberAvailability {
  constructor(public id?: number, public availability?: Availability, public member?: IMember | null, public venue?: IVenue | null) {}
}

export function getMemberAvailabilityIdentifier(memberAvailability: IMemberAvailability): number | undefined {
  return memberAvailability.id;
}
