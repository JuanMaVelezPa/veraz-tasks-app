export interface SortableColumn {
  key: string;
  label: string;
  sortable: boolean;
}

export interface SortOptions {
  sort?: string;
  order?: 'asc' | 'desc';
}
