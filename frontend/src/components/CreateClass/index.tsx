import React, { useState } from 'react';
import { Button, Col, Fade, Form, Modal, ModalProps } from 'react-bootstrap';
import './CreateClassStyle.scss';
import GroupAddTwoToneIcon from '@material-ui/icons/GroupAddTwoTone';

type CreateClassProps = {
  onHide: any;
  show: any;
};

const curYear = new Date().getFullYear();
const axios = require('axios');

const CreateClass: React.FC<CreateClassProps> = ({ onHide, show }: CreateClassProps) => {
  const [screen1, setScreen1] = useState(true);
  const [screen2, setScreen2] = useState(false);

  const [className, setClassName] = useState('');
  const [classNumber, setClassNumber] = useState('');
  const [classDescription, setClassDescription] = useState('');
  const [classTerm, setClassTerm] = useState('');
  const [classYear, setClassYear] = useState('');

  const [classNameError, setClassNameError] = useState('');
  const [classNumberError, setClassNumberError] = useState('');
  const [classDescriptionError, setClassDescriptionError] = useState('');
  const [classTermError, setClassTermError] = useState('');
  const [classYearError, setClassYearError] = useState('');

  const createClass = () => {
    let validPost = true;
    if (className === '') {
      setClassNameError('Please give your class a name');
      validPost = false;
    }
    if (classNumber === '') {
      setClassNumberError('Please give your class a number');
      validPost = false;
    }
    if (classDescription === '') {
      setClassDescriptionError('Please give your class a description');
      validPost = false;
    }
    if (classTerm === '') {
      setClassTermError('Please select a class term');
      validPost = false;
    }
    if (classYear === '') {
      setClassYearError('Please select a class year');
      validPost = false;
    }

    const postParameters = {
      class_name: className,
      class_number: classNumber,
      class_description: classDescription,
      class_term: `${classTerm} ${classYear}`,
    };

    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };

    if (validPost) {
      axios
        .post('http://localhost:4567/create_class', postParameters, config)
        .then((response: any) => {
          if (response.data.status === 0) {
            console.log('Class created!');
          } else {
            console.log('Class creation failed!');
          }
          console.log(response.data);
        })
        .catch((err: any) => {
          console.log(err);
        });
    }
  };

  return (
    <Modal
      onHide={onHide}
      show={show}
      onExited={() => {
        setClassNameError('');
        setClassNumberError('');
        setClassDescriptionError('');
        setClassTermError('');
        setClassYearError('');
        setScreen1(!screen1);
        setScreen2(!screen2);
      }}
      aria-labelledby="contained-modal-title-vcenter"
      scrollable
      centered
      className="create-class-modal"
    >
      <Modal.Header closeButton>Create Class</Modal.Header>
      {screen1 && (
        <Fade in={screen1}>
          <Modal.Body>
            <GroupAddTwoToneIcon className="create-class-icon" />
            <div className="class-header">Class</div>
            <p>
              You don&apos;t have to be in the best group. You just have to be in a better group
              than the guy next to you.
            </p>
            <Button
              variant="primary"
              onClick={() => {
                setScreen1(!screen1);
                setScreen2(!screen2);
              }}
            >
              Create a new class
            </Button>
          </Modal.Body>
        </Fade>
      )}

      {screen2 && (
        <Fade in={screen2}>
          <Modal.Body>
            <div className="class-information" />
            <Form className="class-information-form">
              <Form.Row>
                <Form.Group as={Col} controlId="formGroupClassName">
                  <Form.Label>Class Name</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="e.g. Software Engineering"
                    required
                    isInvalid={classNameError !== ''}
                    onChange={(e: any) => setClassName(e.target.value)}
                  />
                  <Form.Control.Feedback type="invalid">{classNameError}</Form.Control.Feedback>
                </Form.Group>
              </Form.Row>

              <Form.Row>
                <Form.Group as={Col} controlId="formGroupClassNumber">
                  <Form.Label>Class Number</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="e.g. CSCI 0320"
                    required
                    isInvalid={classNumberError !== ''}
                    onChange={(e: any) => setClassNumber(e.target.value)}
                  />
                  <Form.Control.Feedback type="invalid">{classNumberError}</Form.Control.Feedback>
                </Form.Group>
              </Form.Row>

              <Form.Row>
                <Form.Group as={Col} controlId="formGroupClassNumber">
                  <Form.Label>Class Description</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="e.g. I want to die..."
                    required
                    isInvalid={classDescriptionError !== ''}
                    onChange={(e: any) => setClassDescription(e.target.value)}
                  />
                  <Form.Control.Feedback type="invalid">
                    {classDescriptionError}
                  </Form.Control.Feedback>
                </Form.Group>
              </Form.Row>

              <Form.Row>
                <Form.Group as={Col} controlId="formGroupClassTerm">
                  <Form.Label>Term</Form.Label>
                  <Form.Control
                    as="select"
                    isInvalid={classTermError !== ''}
                    onChange={(e: any) => setClassTerm(e.target.value)}
                  >
                    <option>Fall</option>
                    <option>Winter</option>
                    <option>Spring</option>
                    <option>Summer</option>
                  </Form.Control>
                  <Form.Control.Feedback type="invalid">{classTermError}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group as={Col} controlId="formGroupClassTerm">
                  <Form.Label>Year</Form.Label>
                  <Form.Control
                    as="select"
                    isInvalid={classYearError !== ''}
                    onChange={(e: any) => setClassYear(e.target.value)}
                  >
                    <option>{curYear}</option>
                    <option>{curYear + 1}</option>
                    <option>{curYear + 2}</option>
                    <option>{curYear + 3}</option>
                  </Form.Control>
                  <Form.Control.Feedback type="invalid">{classYearError}</Form.Control.Feedback>
                </Form.Group>
              </Form.Row>

              <Form.Row>
                <Form.Group as={Col} controlId="formGroupCreateClassButton">
                  <Button
                    variant="primary"
                    size="sm"
                    onClick={() => {
                      setClassNameError('');
                      setClassNumberError('');
                      setClassDescriptionError('');
                      setClassTermError('');
                      setClassYearError('');
                      createClass();
                    }}
                  >
                    Create
                  </Button>
                </Form.Group>
              </Form.Row>
            </Form>
          </Modal.Body>
        </Fade>
      )}
    </Modal>
  );
};

export default CreateClass;
