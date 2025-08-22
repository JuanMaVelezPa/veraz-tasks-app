# Employee Module Troubleshooting

## Problema: "Person is already an employee" Error

### Descripción del Problema
Cuando intentas crear un empleado, aparece el error "Person is already an employee" aunque no hay empleados en la base de datos.

### Causas Posibles
1. **Cache obsoleto**: El frontend tiene datos cacheados de un empleado anterior
2. **Empleado inactivo**: Existe un empleado "soft deleted" en la base de datos
3. **Sincronización**: Desincronización entre frontend y backend

### Soluciones

#### 1. Limpiar Cache del Frontend
```typescript
// En la consola del navegador:
// Obtener el servicio de empleados
const employeeService = (window as any).ng.getService('EmployeeService');

// Limpiar todo el cache de empleados
employeeService.forceRefreshAllEmployeeCache();

// Ver estadísticas del cache
console.log(employeeService.getCacheStats());
```

#### 2. Verificar Base de Datos
```sql
-- Verificar si hay empleados inactivos
SELECT * FROM ge_templ WHERE empl_pers = 'PERSON_ID_AQUI' AND empl_acti = false;

-- Verificar todos los empleados
SELECT * FROM ge_templ WHERE empl_pers = 'PERSON_ID_AQUI';
```

#### 3. Reiniciar Backend
Si el problema persiste, reinicia el servidor backend para asegurar que los cambios en el repositorio se apliquen.

#### 4. Limpiar Cache del Navegador
- Presiona `Ctrl + Shift + R` para hard refresh
- O ve a DevTools > Application > Storage > Clear storage

### Prevención
- El cache se limpia automáticamente al crear/actualizar/eliminar empleados
- Los métodos del repositorio ahora solo consideran empleados activos (`isActive = true`)
- Se añadió limpieza de cache antes de crear empleados

### Debugging
Para debugging avanzado, puedes usar:
```typescript
// Ver cache detallado
const cacheService = (window as any).ng.getService('CacheService');
console.log(cacheService.getDetailedStats());

// Ver keys específicas de empleados
console.log(cacheService.getKeysMatching('employee:'));
```




