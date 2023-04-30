import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trip-plan.reducer';

export const TripPlanDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tripPlanEntity = useAppSelector(state => state.tripPlan.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tripPlanDetailsHeading">Trip Plan</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tripPlanEntity.id}</dd>
          <dt>
            <span id="tripName">Trip Name</span>
          </dt>
          <dd>{tripPlanEntity.tripName}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>{tripPlanEntity.startDate}</dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>{tripPlanEntity.endDate}</dd>
          <dt>
            <span id="totalDays">Total Days</span>
          </dt>
          <dd>{tripPlanEntity.totalDays}</dd>
          <dt>
            <span id="review">Review</span>
          </dt>
          <dd>{tripPlanEntity.review}</dd>
          <dt>User Profile</dt>
          <dd>{tripPlanEntity.userProfile ? tripPlanEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trip-plan" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trip-plan/${tripPlanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TripPlanDetail;
