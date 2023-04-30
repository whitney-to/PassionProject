import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITripPlan } from 'app/shared/model/trip-plan.model';
import { getEntities as getTripPlans } from 'app/entities/trip-plan/trip-plan.reducer';
import { ITripDay } from 'app/shared/model/trip-day.model';
import { getEntity, updateEntity, createEntity, reset } from './trip-day.reducer';

export const TripDayUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tripPlans = useAppSelector(state => state.tripPlan.entities);
  const tripDayEntity = useAppSelector(state => state.tripDay.entity);
  const loading = useAppSelector(state => state.tripDay.loading);
  const updating = useAppSelector(state => state.tripDay.updating);
  const updateSuccess = useAppSelector(state => state.tripDay.updateSuccess);

  const handleClose = () => {
    navigate('/trip-day');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTripPlans({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tripDayEntity,
      ...values,
      tripPlan: tripPlans.find(it => it.id.toString() === values.tripPlan.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...tripDayEntity,
          tripPlan: tripDayEntity?.tripPlan?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="touristaApp.tripDay.home.createOrEditLabel" data-cy="TripDayCreateUpdateHeading">
            Create or edit a Trip Day
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="trip-day-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Date" id="trip-day-date" name="date" data-cy="date" type="date" />
              <ValidatedField id="trip-day-tripPlan" name="tripPlan" data-cy="tripPlan" label="Trip Plan" type="select">
                <option value="" key="0" />
                {tripPlans
                  ? tripPlans.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trip-day" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TripDayUpdate;
