import { ITripDay } from 'app/shared/model/trip-day.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ITripPlan {
  id?: number;
  tripName?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  totalDays?: number | null;
  review?: string | null;
  tripDays?: ITripDay[] | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<ITripPlan> = {};
