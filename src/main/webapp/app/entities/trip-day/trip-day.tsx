import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITripDay } from 'app/shared/model/trip-day.model';
import { getEntities } from './trip-day.reducer';

export const TripDay = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const tripDayList = useAppSelector(state => state.tripDay.entities);
  const loading = useAppSelector(state => state.tripDay.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="trip-day-heading" data-cy="TripDayHeading">
        Trip Days
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/trip-day/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Trip Day
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {tripDayList && tripDayList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Date</th>
                <th>Trip Plan</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tripDayList.map((tripDay, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/trip-day/${tripDay.id}`} color="link" size="sm">
                      {tripDay.id}
                    </Button>
                  </td>
                  <td>{tripDay.date ? <TextFormat type="date" value={tripDay.date} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{tripDay.tripPlan ? <Link to={`/trip-plan/${tripDay.tripPlan.id}`}>{tripDay.tripPlan.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/trip-day/${tripDay.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/trip-day/${tripDay.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/trip-day/${tripDay.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Trip Days found</div>
        )}
      </div>
    </div>
  );
};

export default TripDay;
