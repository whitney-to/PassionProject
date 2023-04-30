import dayjs from 'dayjs';
import { IActivity } from 'app/shared/model/activity.model';
import { ITripPlan } from 'app/shared/model/trip-plan.model';

export interface ITripDay {
  id?: number;
  date?: string | null;
  activities?: IActivity[] | null;
  tripPlan?: ITripPlan | null;
}

export const defaultValue: Readonly<ITripDay> = {};
