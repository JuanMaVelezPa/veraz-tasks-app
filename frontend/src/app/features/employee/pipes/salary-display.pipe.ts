import { Pipe, PipeTransform, inject } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { EmployeeParamsService } from '@employee/services/employee-params.service';

@Pipe({
  name: 'salaryDisplay',
  standalone: true,
  pure: true // Optimización: pipe puro para mejor performance
})
export class SalaryDisplayPipe implements PipeTransform {
  private employeeParamsService = inject(EmployeeParamsService);
  private currencyPipe = new CurrencyPipe('en-US'); // Reutilizar instancia
  private currencyCache = new Map<string, string>(); // Cache para símbolos

  transform(salary: number | null | undefined, currency: string | null | undefined, salaryType: string | null | undefined): string {
    if (!salary || salary <= 0) {
      return 'N/A';
    }

    const currencyCode = currency || 'USD';
    const type = salaryType || 'MONTHLY';

    // Formatear el salario de manera compacta
    const formattedSalary = this.formatCompactSalary(salary, currencyCode);

    // Obtener la frecuencia en formato legible
    const frequency = this.getFrequencyDisplay(type);

    return `${formattedSalary}/${frequency}`;
  }

      private formatCompactSalary(salary: number, currencyCode: string): string {
    const currencySymbol = this.getCurrencySymbolFromCache(currencyCode);

    if (salary >= 1000000) {
      // Millones - mostrar con 1 decimal
      const millions = Math.round((salary / 1000000) * 10) / 10;
      return `${currencySymbol}${millions}M`;
    } else if (salary >= 1000) {
      // Miles - mostrar con 1 decimal
      const thousands = Math.round((salary / 1000) * 10) / 10;
      return `${currencySymbol}${thousands}K`;
    } else {
      // Menos de 1000 - mostrar completo
      return this.currencyPipe.transform(salary, currencyCode, 'symbol', '1.0-0') || `${currencySymbol}${salary}`;
    }
  }

  private getCurrencySymbolFromCache(currencyCode: string): string {
    // Usar cache para evitar búsquedas repetidas
    if (this.currencyCache.has(currencyCode)) {
      return this.currencyCache.get(currencyCode)!;
    }

    const currency = this.employeeParamsService.getCurrencies()().find(c => c.code === currencyCode);
    const symbol = currency?.symbol || '$';
    this.currencyCache.set(currencyCode, symbol);
    return symbol;
  }

  private getFrequencyDisplay(salaryType: string): string {
    // Cache estático para frecuencias - no cambian
    const frequencyMap: Record<string, string> = {
      'HOURLY': 'Hour',
      'MONTHLY': 'Month',
      'ANNUAL': 'Year'
    };

    return frequencyMap[salaryType] || 'Month';
  }
}
