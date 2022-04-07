export interface ISong {
  id?: number;
  name?: string;
  duration?: string;
  score?: string | null;
}

export class Song implements ISong {
  constructor(public id?: number, public name?: string, public duration?: string, public score?: string | null) {}
}

export function getSongIdentifier(song: ISong): number | undefined {
  return song.id;
}
