import { ILocation } from 'app/shared/model/location.model';
import { ITripDay } from 'app/shared/model/trip-day.model';

export interface IActivity {
  id?: number;
  time?: string | null;
  location?: ILocation | null;
  tripDay?: ITripDay | null;
}

export const defaultValue: Readonly<IActivity> = {};
