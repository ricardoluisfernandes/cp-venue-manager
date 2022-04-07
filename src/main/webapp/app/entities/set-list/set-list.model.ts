import { IVenue } from 'app/entities/venue/venue.model';
import { ISetListLine } from 'app/entities/set-list-line/set-list-line.model';

export interface ISetList {
  id?: number;
  description?: string;
  totalDuration?: string | null;
  venue?: IVenue | null;
  setListLines?: ISetListLine[] | null;
}

export class SetList implements ISetList {
  constructor(
    public id?: number,
    public description?: string,
    public totalDuration?: string | null,
    public venue?: IVenue | null,
    public setListLines?: ISetListLine[] | null
  ) {}
}

export function getSetListIdentifier(setList: ISetList): number | undefined {
  return setList.id;
}
