import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TripPlan from './trip-plan';
import TripPlanDetail from './trip-plan-detail';
import TripPlanUpdate from './trip-plan-update';
import TripPlanDeleteDialog from './trip-plan-delete-dialog';

const TripPlanRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TripPlan />} />
    <Route path="new" element={<TripPlanUpdate />} />
    <Route path=":id">
      <Route index element={<TripPlanDetail />} />
      <Route path="edit" element={<TripPlanUpdate />} />
      <Route path="delete" element={<TripPlanDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TripPlanRoutes;
