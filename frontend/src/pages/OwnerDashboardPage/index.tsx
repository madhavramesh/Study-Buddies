import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import CreateClass from '../../components/CreateClass';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';

const OwnerDashboardPage: React.FC = () => {
  const [modalShow, setModalShow] = useState(true);

  return (
    <div className="owner-dashboard-page">
      <ModifiedNavBar username="Madhav Ramesh" />

      <div className="class-created-modal-container">
        <ClassCreatedModal
          onHide={() => setModalShow(false)}
          show={modalShow}
          className="CSCI 0320"
          classDescription="I want to die. Blah blah blah. Adding more text to make this look better."
        />
      </div>
    </div>
  );
};

export default OwnerDashboardPage;
