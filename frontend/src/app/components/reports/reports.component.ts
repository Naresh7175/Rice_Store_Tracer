import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
selector: 'app-reports',
standalone: true,
imports: [CommonModule, FormsModule],
templateUrl: './reports.component.html',
styleUrl: './reports.component.css'
})
export class ReportsComponent implements OnInit {

/* ================== Filters ================== */
period: string = 'monthly';
currentFilter: string = 'ALL';
searchTerm: string = '';

selectedMonth: number | null = null;
selectedYear: number | null = null;

months = [
{ val: 1, name: 'January' }, { val: 2, name: 'February' },
{ val: 3, name: 'March' }, { val: 4, name: 'April' },
{ val: 5, name: 'May' }, { val: 6, name: 'June' },
{ val: 7, name: 'July' }, { val: 8, name: 'August' },
{ val: 9, name: 'September' }, { val: 10, name: 'October' },
{ val: 11, name: 'November' }, { val: 12, name: 'December' }
];

years: number[] = [];

/* ================== Data ================== */
sales: any[] = [];
filteredSales: any[] = [];

/* ================== Dashboard ================== */
dashboardStats = {
revenue: 0,
bagsSold: 0,
pendingMoney: 0
};

/* ================== Footer Totals ================== */
totalBagsSold = 0;
totalDiscount = 0;
totalPending = 0;
totalPaid = 0;
totalAmount = 0;

constructor(private apiService: ApiService) {
    const currentYear = new Date().getFullYear();
    for (let i = currentYear; i >= 2024; i--) {
      this.years.push(i);
    }

    this.selectedMonth = new Date().getMonth() + 1;
    this.selectedYear = currentYear;
  }

  ngOnInit(): void {
    this.loadData();
  }

  /* ================== Data Loading ================== */
  loadData(): void {
    const month = this.period === 'monthly' ? this.selectedMonth ?? undefined : undefined;
    const year = this.period === 'monthly' ? this.selectedYear ?? undefined : undefined;

    this.apiService.getSales(this.period, month, year).subscribe({
      next: (data) => {
        this.sales = data || [];
        this.applyFilter();
      },
      error: () => {
        this.sales = [];
        this.applyFilter();
      }
    });
  }

  /* ================== Filters ================== */
  filterBy(type: string): void {
    this.currentFilter = type;
    this.applyFilter();
  }

  applyFilter(): void {
    let temp = [...this.sales];

    if (this.currentFilter === 'PENDING') {
      temp = temp.filter(s => s.balance > 0);
    }

    if (this.searchTerm?.trim()) {
      const term = this.searchTerm.toLowerCase();
      temp = temp.filter(s =>
        s.customer?.name?.toLowerCase().includes(term)
      );
    }

    this.filteredSales = temp;
    this.calculateTotals();
  }

  /* ================== Aggregations ================== */
  calculateTotals(): void {
    this.totalBagsSold = 0;
    this.totalDiscount = 0;
    this.totalPending = 0;
    this.totalPaid = 0;
    this.totalAmount = 0;

    this.filteredSales.forEach(sale => {

      this.totalDiscount += sale.discount || 0;
      this.totalPending += sale.balance || 0;
      this.totalPaid += sale.paidAmount || 0;
      this.totalAmount += sale.totalAmount || 0;

      if (sale.items?.length) {
        sale.items.forEach((item: any) => {
          if (item.unit === 'BAG') {
            this.totalBagsSold += item.quantity || 0;
          }
        });
      }
    });

    /* Dashboard Cards */
    this.dashboardStats = {
      revenue: this.totalPaid,     // collected money
      bagsSold: this.totalBagsSold,
      pendingMoney: this.totalPending
    };
  }
}
