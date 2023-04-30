import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TripDay from './trip-day';
import TripDayDetail from './trip-day-detail';
import TripDayUpdate from './trip-day-update';
import TripDayDeleteDialog from './trip-day-delete-dialog';

const TripDayRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TripDay />} />
    <Route path="new" element={<TripDayUpdate />} />
    <Route path=":id">
      <Route index element={<TripDayDetail />} />
      <Route path="edit" element={<TripDayUpdate />} />
      <Route path="delete" element={<TripDayDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TripDayRoutes;
