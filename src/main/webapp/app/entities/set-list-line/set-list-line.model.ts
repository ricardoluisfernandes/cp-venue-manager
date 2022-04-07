import { ISong } from 'app/entities/song/song.model';
import { ISetList } from 'app/entities/set-list/set-list.model';

export interface ISetListLine {
  id?: number;
  order?: number;
  song?: ISong | null;
  setList?: ISetList | null;
}

export class SetListLine implements ISetListLine {
  constructor(public id?: number, public order?: number, public song?: ISong | null, public setList?: ISetList | null) {}
}

export function getSetListLineIdentifier(setListLine: ISetListLine): number | undefined {
  return setListLine.id;
}
