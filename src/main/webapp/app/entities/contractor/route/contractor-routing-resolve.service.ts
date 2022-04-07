import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContractor, Contractor } from '../contractor.model';
import { ContractorService } from '../service/contractor.service';

@Injectable({ providedIn: 'root' })
export class ContractorRoutingResolveService implements Resolve<IContractor> {
  constructor(protected service: ContractorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContractor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contractor: HttpResponse<Contractor>) => {
          if (contractor.body) {
            return of(contractor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Contractor());
  }
}
