export interface SortField {
  key: string;
  label: string;
}

export interface PaginationOptions {
  page: number;
  size: number;
  sort?: string;
  order?: 'asc' | 'desc';
}

export interface PaginationInfo {
  currentPage: number;
  totalPages: number;
  totalElements: number;
  pageSize: number;
  hasNext: boolean;
  hasPrevious: boolean;
  isFirst: boolean;
  isLast: boolean;
}

// Interfaz para la respuesta paginada del backend
export interface PaginatedResponseDTO<T> {
  data: T[];
  pagination: PaginationInfo;
}
