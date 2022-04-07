import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISetList, SetList } from '../set-list.model';
import { SetListService } from '../service/set-list.service';

@Injectable({ providedIn: 'root' })
export class SetListRoutingResolveService implements Resolve<ISetList> {
  constructor(protected service: SetListService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISetList> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((setList: HttpResponse<SetList>) => {
          if (setList.body) {
            return of(setList.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SetList());
  }
}
