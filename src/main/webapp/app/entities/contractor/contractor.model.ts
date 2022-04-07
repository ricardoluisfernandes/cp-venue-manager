export interface IContractor {
  id?: number;
  name?: string;
  email?: string | null;
  phoneNumber?: string | null;
  comments?: string | null;
}

export class Contractor implements IContractor {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string | null,
    public phoneNumber?: string | null,
    public comments?: string | null
  ) {}
}

export function getContractorIdentifier(contractor: IContractor): number | undefined {
  return contractor.id;
}
