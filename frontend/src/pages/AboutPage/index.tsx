import React from 'react';
import './AboutStyle.scss';
import { Button } from 'react-bootstrap';
import NavBar from '../../components/Navbar';
import Footer from '../../components/Footer';

const AboutPage: React.FC = () => {
  return (
    <div style={{ overflow: 'hidden' }}>
      <NavBar />
      <div className="aboutpage-container">
        <div id="title">
          <h1 id="title-text">How it works</h1>
          <p>Usage | Algorithm </p>
        </div>
        <div id="explanation">
          <h1>Our algorithm follows four steps:</h1>
          <ol>
            <li>
              Based on each students&apos; preferences, we use a heuristic to estimate the
              compatibility of each pair of students.
            </li>
            <li>
              Using this compatibility score, a complete graph is generated with students as nodes
              and the compatibility score as the edge weight between any two nodes. The
              compatibility of a group of students is sum of the weights of all the them.
            </li>
            <li>
              Groups are recursively created by solving for the set of nodes of the required size
              which has the maximum possible compatibility, deleting that group, then finding the
              next most compatible group, and so on.
            </li>
            <li>
              If at the end there are not enough students to form a full group, they are each added
              to the latest formed previous groups.
            </li>
          </ol>
        </div>
        <div id="heuristic">
          <h1>How compatibility is estimated:</h1>
          <p>
            There are three components of preferences: dorm location, preferred (or not) partners,
            and preferred working times. Each component is weighted so that the final compatibility
            score between any two people lies between 0 and 100 inclusive.
          </p>
          <ul>
            <li>
              40% - Working Times:
              <ul>
                <li>
                  If two people have overlapping working times greater than or equal 35 hours, they
                  receive the entirety of this 40%.
                </li>
                <li>
                  If two persons overlapping working time in a week is less than 35 hours, they
                  receive ((number of overlapping hours / 35) x 40)%
                </li>
              </ul>
            </li>
            <li>
              40% - People Preferences:
              <ul>
                <li>
                  If any one person is uncomfortable working with someone else, they receive no
                  weight through this component.
                </li>
                <li>If both students feel neutral about each other, then they receive 20%.</li>
                <li>
                  If only one student explicitly wants to work with the other, but the other feels
                  neutral, then they receive 30%.
                </li>
                <li>If two people prefer each other, they receive the entirety of this 40%.</li>
              </ul>
            </li>
            <li>
              20% - Dorm Locations:
              <ul>
                <li>
                  The percentage of the weight metric that is received from dorm locations is 20% x
                  (Distance between dorms / Maximum possible distance between two dorms)
                </li>
              </ul>
            </li>
          </ul>
        </div>
        <div id="usage">
          <h1>Usage:</h1>
          <div className="usage-text">
            <p>
              Chunky study buddies is an all-in-one solution to matching students into the most
              compatible study groups. Here&apos;s how to use our app!
            </p>
            <p>
              First, sign up or log in to your account. This will take you to your dashboard, where
              you can view your enrolled classes, as well as all classes available at the moment. To
              join a class, get the class code from the user, and enter it where indicated. If you
              have already joined the class, or are the owner, you will see an option to view the
              class&apos;s dashboard.
            </p>
            <p>
              If you are a student, you will see a page containing detailed information about the
              class, as well as all of the students in the class. You will also see an option to
              fill out preferences; this will help determine which study group you&apos;ll be put
              in!
            </p>
            <p>
              If you are the owner, you will see a similar page, except instead of an button to set
              preferences, you can form study groups. Use this to divide people up, and start
              studying!
            </p>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default AboutPage;
