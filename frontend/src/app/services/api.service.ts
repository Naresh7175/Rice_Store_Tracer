import{Injectable}from'@angular/core';
import {HttpClient}from '@angular/common/http';
import {Observable} from 'rxjs';

export interface Product {
id?: number;
name: string;
brand: string;
quantity: number;
price: number;
description?: string;
image?: string;
}

export interface Customer {
id?: number;
name: string;
phone: string;
address: string;
totalDebt: number;
}

export interface SaleItemRequest {
productId: number;
quantity: number;
unit?: string;
}

export interface SaleRequest {
customerId: number;
items: SaleItemRequest[];
discount: number;
paidAmount: number;
}

@Injectable({
providedIn: 'root'
})
export class ApiService {
private baseUrl = 'https://rice-store-tracer-backend.onrender.com/api';

constructor(private http: HttpClient) { }

    // Inventory
    getProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(`${this.baseUrl}/inventory`);
    }

    addProduct(product: Product): Observable<Product> {
        return this.http.post<Product>(`${this.baseUrl}/inventory`, product);
    }

    // Customers
    getCustomers(): Observable<Customer[]> {
        return this.http.get<Customer[]>(`${this.baseUrl}/customers`);
    }

    addCustomer(customer: Customer): Observable<Customer> {
        return this.http.post<Customer>(`${this.baseUrl}/customers`, customer);
    }

    // Sales
    createSale(sale: SaleRequest): Observable<any> {
        return this.http.post<any>(`${this.baseUrl}/sales`, sale);
    }

    // Reports
    getRevenue(period: string, month?: number, year?: number): Observable<number> {
        let url = `${this.baseUrl}/reports/revenue?period=${period}`;
        if (month) url += `&month=${month}`;
        if (year) url += `&year=${year}`;
        return this.http.get<number>(url);
    }

    getSales(month?: number, year?: number) {
  let params: any = {};

  if (month !== undefined) {
    params.month = month;
  }

  if (year !== undefined) {
    params.year = year;
  }

  return this.http.get<any[]>('/api/sales', { params });
}


    getDashboardStats(): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/reports/dashboard`);
    }

    // Removed uploadImage mechanism as we use base64 now
}

