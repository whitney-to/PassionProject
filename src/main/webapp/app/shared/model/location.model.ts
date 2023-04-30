export interface ILocation {
  id?: number;
  locationName?: string | null;
  latitude?: number | null;
  longitude?: number | null;
}

export const defaultValue: Readonly<ILocation> = {};
