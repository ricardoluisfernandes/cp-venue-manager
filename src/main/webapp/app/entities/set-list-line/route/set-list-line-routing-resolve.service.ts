import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISetListLine, SetListLine } from '../set-list-line.model';
import { SetListLineService } from '../service/set-list-line.service';

@Injectable({ providedIn: 'root' })
export class SetListLineRoutingResolveService implements Resolve<ISetListLine> {
  constructor(protected service: SetListLineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISetListLine> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((setListLine: HttpResponse<SetListLine>) => {
          if (setListLine.body) {
            return of(setListLine.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SetListLine());
  }
}
