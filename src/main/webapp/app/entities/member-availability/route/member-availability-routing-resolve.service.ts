import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMemberAvailability, MemberAvailability } from '../member-availability.model';
import { MemberAvailabilityService } from '../service/member-availability.service';

@Injectable({ providedIn: 'root' })
export class MemberAvailabilityRoutingResolveService implements Resolve<IMemberAvailability> {
  constructor(protected service: MemberAvailabilityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMemberAvailability> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((memberAvailability: HttpResponse<MemberAvailability>) => {
          if (memberAvailability.body) {
            return of(memberAvailability.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MemberAvailability());
  }
}
