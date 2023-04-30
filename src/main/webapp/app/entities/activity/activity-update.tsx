import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { ITripDay } from 'app/shared/model/trip-day.model';
import { getEntities as getTripDays } from 'app/entities/trip-day/trip-day.reducer';
import { IActivity } from 'app/shared/model/activity.model';
import { getEntity, updateEntity, createEntity, reset } from './activity.reducer';

export const ActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const locations = useAppSelector(state => state.location.entities);
  const tripDays = useAppSelector(state => state.tripDay.entities);
  const activityEntity = useAppSelector(state => state.activity.entity);
  const loading = useAppSelector(state => state.activity.loading);
  const updating = useAppSelector(state => state.activity.updating);
  const updateSuccess = useAppSelector(state => state.activity.updateSuccess);

  const handleClose = () => {
    navigate('/activity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLocations({}));
    dispatch(getTripDays({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...activityEntity,
      ...values,
      location: locations.find(it => it.id.toString() === values.location.toString()),
      tripDay: tripDays.find(it => it.id.toString() === values.tripDay.toString()),
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
          ...activityEntity,
          location: activityEntity?.location?.id,
          tripDay: activityEntity?.tripDay?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="touristaApp.activity.home.createOrEditLabel" data-cy="ActivityCreateUpdateHeading">
            Create or edit a Activity
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="activity-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Time" id="activity-time" name="time" data-cy="time" type="text" />
              <ValidatedField id="activity-location" name="location" data-cy="location" label="Location" type="select">
                <option value="" key="0" />
                {locations
                  ? locations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.locationName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="activity-tripDay" name="tripDay" data-cy="tripDay" label="Trip Day" type="select">
                <option value="" key="0" />
                {tripDays
                  ? tripDays.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activity" replace color="info">
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

export default ActivityUpdate;
