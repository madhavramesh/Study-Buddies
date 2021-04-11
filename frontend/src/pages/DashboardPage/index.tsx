import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import { Create } from '@material-ui/icons';
import CreateClass from '../../components/CreateClass';
import './DashboardPageStyle.scss';

const DashboardPage: React.FC = () => {
  const [modalShow, setModalShow] = useState(false);

  return (
    <div className="dashboard-page">
      <div className="create-class-container">
        <Button variant="primary" onClick={() => setModalShow(true)}>
          Create class
        </Button>
        <CreateClass show={modalShow} onHide={() => setModalShow(false)} />
      </div>
    </div>
  );
};

export default DashboardPage;
