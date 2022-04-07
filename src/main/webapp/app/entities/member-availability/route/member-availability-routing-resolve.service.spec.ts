import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMemberAvailability, MemberAvailability } from '../member-availability.model';
import { MemberAvailabilityService } from '../service/member-availability.service';

import { MemberAvailabilityRoutingResolveService } from './member-availability-routing-resolve.service';

describe('MemberAvailability routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MemberAvailabilityRoutingResolveService;
  let service: MemberAvailabilityService;
  let resultMemberAvailability: IMemberAvailability | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(MemberAvailabilityRoutingResolveService);
    service = TestBed.inject(MemberAvailabilityService);
    resultMemberAvailability = undefined;
  });

  describe('resolve', () => {
    it('should return IMemberAvailability returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMemberAvailability = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMemberAvailability).toEqual({ id: 123 });
    });

    it('should return new IMemberAvailability if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMemberAvailability = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMemberAvailability).toEqual(new MemberAvailability());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MemberAvailability })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMemberAvailability = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMemberAvailability).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
