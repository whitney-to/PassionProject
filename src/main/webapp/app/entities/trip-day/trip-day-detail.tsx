import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trip-day.reducer';

export const TripDayDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tripDayEntity = useAppSelector(state => state.tripDay.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tripDayDetailsHeading">Trip Day</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tripDayEntity.id}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{tripDayEntity.date ? <TextFormat value={tripDayEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>Trip Plan</dt>
          <dd>{tripDayEntity.tripPlan ? tripDayEntity.tripPlan.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trip-day" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trip-day/${tripDayEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TripDayDetail;
