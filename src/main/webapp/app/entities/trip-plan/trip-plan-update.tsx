import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { ITripPlan } from 'app/shared/model/trip-plan.model';
import { getEntity, updateEntity, createEntity, reset } from './trip-plan.reducer';

export const TripPlanUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const tripPlanEntity = useAppSelector(state => state.tripPlan.entity);
  const loading = useAppSelector(state => state.tripPlan.loading);
  const updating = useAppSelector(state => state.tripPlan.updating);
  const updateSuccess = useAppSelector(state => state.tripPlan.updateSuccess);

  const handleClose = () => {
    navigate('/trip-plan');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tripPlanEntity,
      ...values,
      userProfile: userProfiles.find(it => it.id.toString() === values.userProfile.toString()),
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
          ...tripPlanEntity,
          userProfile: tripPlanEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="touristaApp.tripPlan.home.createOrEditLabel" data-cy="TripPlanCreateUpdateHeading">
            Create or edit a Trip Plan
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="trip-plan-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Trip Name" id="trip-plan-tripName" name="tripName" data-cy="tripName" type="text" />
              <ValidatedField label="Start Date" id="trip-plan-startDate" name="startDate" data-cy="startDate" type="text" />
              <ValidatedField label="End Date" id="trip-plan-endDate" name="endDate" data-cy="endDate" type="text" />
              <ValidatedField label="Total Days" id="trip-plan-totalDays" name="totalDays" data-cy="totalDays" type="text" />
              <ValidatedField label="Review" id="trip-plan-review" name="review" data-cy="review" type="text" />
              <ValidatedField id="trip-plan-userProfile" name="userProfile" data-cy="userProfile" label="User Profile" type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trip-plan" replace color="info">
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

export default TripPlanUpdate;
