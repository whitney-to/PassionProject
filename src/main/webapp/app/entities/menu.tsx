import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        User Profile
      </MenuItem>
      <MenuItem icon="asterisk" to="/trip-plan">
        Trip Plan
      </MenuItem>
      <MenuItem icon="asterisk" to="/location">
        Location
      </MenuItem>
      <MenuItem icon="asterisk" to="/trip-day">
        Trip Day
      </MenuItem>
      <MenuItem icon="asterisk" to="/activity">
        Activity
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
