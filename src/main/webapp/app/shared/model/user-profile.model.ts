import { IUser } from 'app/shared/model/user.model';
import { ITripPlan } from 'app/shared/model/trip-plan.model';

export interface IUserProfile {
  id?: number;
  phone?: string | null;
  user?: IUser | null;
  plans?: ITripPlan[] | null;
  friends?: IUserProfile[] | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
