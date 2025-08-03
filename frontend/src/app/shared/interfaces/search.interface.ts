export interface SearchOptions {
  page: number;
  size: number;
  sort?: string;
  order?: 'asc' | 'desc';
  search?: string;
}
