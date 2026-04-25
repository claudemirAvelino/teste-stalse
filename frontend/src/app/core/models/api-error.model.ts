export interface ApiError {
  readonly status: number;
  readonly title: string;
  readonly detail: string;
  readonly errors?: Readonly<Record<string, string>>;
}
