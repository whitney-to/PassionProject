import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserProfile from './user-profile';
import TripPlan from './trip-plan';
import Location from './location';
import TripDay from './trip-day';
import Activity from './activity';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="trip-plan/*" element={<TripPlan />} />
        <Route path="location/*" element={<Location />} />
        <Route path="trip-day/*" element={<TripDay />} />
        <Route path="activity/*" element={<Activity />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
