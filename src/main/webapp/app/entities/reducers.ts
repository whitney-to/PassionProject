import userProfile from 'app/entities/user-profile/user-profile.reducer';
import tripPlan from 'app/entities/trip-plan/trip-plan.reducer';
import location from 'app/entities/location/location.reducer';
import tripDay from 'app/entities/trip-day/trip-day.reducer';
import activity from 'app/entities/activity/activity.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  tripPlan,
  location,
  tripDay,
  activity,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
