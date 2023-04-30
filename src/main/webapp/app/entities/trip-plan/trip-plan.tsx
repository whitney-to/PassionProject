import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITripPlan } from 'app/shared/model/trip-plan.model';
import { getEntities } from './trip-plan.reducer';

export const TripPlan = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const tripPlanList = useAppSelector(state => state.tripPlan.entities);
  const loading = useAppSelector(state => state.tripPlan.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="trip-plan-heading" data-cy="TripPlanHeading">
        Trip Plans
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/trip-plan/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Trip Plan
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {tripPlanList && tripPlanList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Trip Name</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Total Days</th>
                <th>Review</th>
                <th>User Profile</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tripPlanList.map((tripPlan, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/trip-plan/${tripPlan.id}`} color="link" size="sm">
                      {tripPlan.id}
                    </Button>
                  </td>
                  <td>{tripPlan.tripName}</td>
                  <td>{tripPlan.startDate}</td>
                  <td>{tripPlan.endDate}</td>
                  <td>{tripPlan.totalDays}</td>
                  <td>{tripPlan.review}</td>
                  <td>
                    {tripPlan.userProfile ? <Link to={`/user-profile/${tripPlan.userProfile.id}`}>{tripPlan.userProfile.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/trip-plan/${tripPlan.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/trip-plan/${tripPlan.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/trip-plan/${tripPlan.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Trip Plans found</div>
        )}
      </div>
    </div>
  );
};

export default TripPlan;
