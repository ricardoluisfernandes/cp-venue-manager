import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpense, getExpenseIdentifier } from '../expense.model';

export type EntityResponseType = HttpResponse<IExpense>;
export type EntityArrayResponseType = HttpResponse<IExpense[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expenses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expense: IExpense): Observable<EntityResponseType> {
    return this.http.post<IExpense>(this.resourceUrl, expense, { observe: 'response' });
  }

  update(expense: IExpense): Observable<EntityResponseType> {
    return this.http.put<IExpense>(`${this.resourceUrl}/${getExpenseIdentifier(expense) as number}`, expense, { observe: 'response' });
  }

  partialUpdate(expense: IExpense): Observable<EntityResponseType> {
    return this.http.patch<IExpense>(`${this.resourceUrl}/${getExpenseIdentifier(expense) as number}`, expense, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpense[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExpenseToCollectionIfMissing(expenseCollection: IExpense[], ...expensesToCheck: (IExpense | null | undefined)[]): IExpense[] {
    const expenses: IExpense[] = expensesToCheck.filter(isPresent);
    if (expenses.length > 0) {
      const expenseCollectionIdentifiers = expenseCollection.map(expenseItem => getExpenseIdentifier(expenseItem)!);
      const expensesToAdd = expenses.filter(expenseItem => {
        const expenseIdentifier = getExpenseIdentifier(expenseItem);
        if (expenseIdentifier == null || expenseCollectionIdentifiers.includes(expenseIdentifier)) {
          return false;
        }
        expenseCollectionIdentifiers.push(expenseIdentifier);
        return true;
      });
      return [...expensesToAdd, ...expenseCollection];
    }
    return expenseCollection;
  }
}
