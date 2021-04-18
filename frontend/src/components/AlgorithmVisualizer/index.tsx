import React from 'react';
import { Button, Modal, Table } from 'react-bootstrap';
import './AlgorithmVisualizerStyle.scss';

/**
 * COPIED FROM https://stackoverflow.com/questions/2353211/hsl-to-rgb-color-conversion
 * Converts an HSL color value to RGB. Conversion formula
 * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
 * Assumes h, s, and l are contained in the set [0, 1] and
 * returns r, g, and b in the set [0, 255].
 *
 * @param   {number}  h       The hue
 * @param   {number}  s       The saturation
 * @param   {number}  l       The lightness
 * @return  {Array}           The RGB representation
 */
const hslToRgb = (h: number, s: number, l: number) => {
  let r;
  let g;
  let b;

  if (s === 0) {
    r = g;
    g = b;
    b = l; // achromatic
  } else {
    const hue2rgb = function hue2rgb(p: number, q: number, t: number) {
      if (t < 0) {
        // eslint-disable-next-line no-param-reassign
        t += 1;
      }
      if (t > 1) {
        // eslint-disable-next-line no-param-reassign
        t -= 1;
      }
      if (t < 1 / 6) return p + (q - p) * 6 * t;
      if (t < 1 / 2) return q;
      if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6;
      return p;
    };

    const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
    const p = 2 * l - q;
    r = hue2rgb(p, q, h + 1 / 3);
    g = hue2rgb(p, q, h);
    b = hue2rgb(p, q, h - 1 / 3);
  }

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  return [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)];
};

/**
 * COPIED FROM https://stackoverflow.com/questions/17525215/calculate-color-values-from-green-to-red
 * Convert a number to a color using hsl, with range definition.
 * Example: if min/max are 0/1, and i is 0.75, the color is closer to green.
 * Example: if min/max are 0.5/1, and i is 0.75, the color is in the middle between red and green.
 * @param i (floating point, range 0 to 1)
 * param min (floating point, range 0 to 1, all i at and below this is red)
 * param max (floating point, range 0 to 1, all i at and above this is green)
 */
const numberToColorHsl = (i: number, min: number, max: number) => {
  let ratio = i;
  if (min >= 0 || max <= 1) {
    if (i < min) {
      ratio = 0;
    } else if (i > max) {
      ratio = 1;
    } else {
      const range = max - min;
      ratio = (i - min) / range;
    }
  }

  // as the function expects a value between 0 and 1, and red = 0° and green = 120°
  // we convert the input to the appropriate hue value
  const hue = (ratio * 1.5) / 3.6;

  // if (minMaxFactor!=1) hue /= minMaxFactor;
  // console.log(hue);

  // we convert hsl to rgb (saturation 100%, lightness 50%)
  const rgb = hslToRgb(hue, 0.65, 0.5);
  // we format to css value and return
  return `rgb(${rgb[0]},${rgb[1]},${rgb[2]})`;
};

/*
const START_COLOR = [251, 110, 80];
const END_COLOR = [105, 251, 80];
const weightToColor = (w: number, startColor: Array<number>, endColor: Array<number>) => {
  const newR = Math.round(w * (endColor[0] - startColor[0]) + startColor[0]);
  const newG = Math.round(w * (endColor[1] - startColor[1]) + startColor[1]);
  const newB = Math.round(w * (endColor[2] - startColor[2]) + startColor[2]);

  return `rgb(${newR}, ${newG}, ${newB})`;
};
*/

// Renders a single group
const renderGroup = (g: any, index: number, studyGroupWeights: any) => {
  // map ids to persons
  const idToPerson: any = {};
  g.forEach((person: any) => {
    idToPerson[person.second.id] = person;
  });

  const graphArray = [];

  const studyGroupWeight: Array<Array<number>> = studyGroupWeights[index];
  // eslint-disable-next-line guard-for-in,no-restricted-syntax
  for (const key in studyGroupWeight) {
    const row = [];
    // eslint-disable-next-line guard-for-in,no-restricted-syntax
    for (const key2 in studyGroupWeight[key]) {
      const newArray = [key, key2, studyGroupWeight[key][key2].toFixed(2)];
      row.push(newArray);
    }
    row.sort((a: any, b: any) => a[1] - b[1]);
    graphArray.push(row);
  }

  graphArray.sort((a: any, b: any) => a[0] - b[0]);

  // eslint-disable-next-line no-plusplus
  for (let i = 0; i < graphArray.length; i++) {
    graphArray[i].splice(i, 0, []);
  }

  const people = g.map((person: any) => idToPerson[person.second.id]);

  return (
    <div className="algorithm-visualizer">
      <Table responsive striped bordered hover>
        <thead>
          <th> </th>
          {people.map((person: any) => {
            return (
              <th id={person.second.id}>
                {person.second.firstName} {person.second.lastName}
              </th>
            );
          })}
        </thead>
        <tbody>
          {graphArray.map((row, rowIndex) => {
            return (
              <tr>
                <td>
                  {people[rowIndex].second.firstName} {people[rowIndex].second.lastName}
                </td>
                {row.map((column, _) => {
                  const color = numberToColorHsl(parseFloat(column[2]), 0, 1);
                  return <td style={{ backgroundColor: color }}>{column?.[2]}</td>;
                })}
              </tr>
            );
          })}
        </tbody>
      </Table>
    </div>
  );
};

type AlgorithmVisualizerProps = {
  studyGroups: any;
  studyGroupWeights: any;
  showModal: boolean;
  setShowModal: any;
};

const AlgorithmVisualizer: React.FC<AlgorithmVisualizerProps> = ({
  studyGroups,
  studyGroupWeights,
  showModal,
  setShowModal,
}: AlgorithmVisualizerProps) => {
  return (
    <>
      <Button className="visualize-groups-button" onClick={() => setShowModal(true)}>
        View Algorithm
      </Button>
      <Modal
        show={showModal}
        onHide={() => {
          setShowModal(false);
        }}
        size="lg"
      >
        <Modal.Header closeButton>
          <Modal.Title>Algorithm Visualization</Modal.Title>
        </Modal.Header>
        <Modal.Body className="algorithm-visualize-modal-body">
          <div className="algorithm-description">
            Each table below represents one study group. Decimals in each cell indicate the
            compatability between two people (1 is the highest and 0 is the lowest). Each cell is
            also color coded, with <span style={{ color: '#3cb371' }}>green</span> representing a
            higher compatibility and <span style={{ color: 'var(--slim-red)' }}>red</span>{' '}
            representing a lower compatibility.
          </div>
          <div>
            {studyGroups.map((group: any, index: number) => {
              return renderGroup(group, index, studyGroupWeights);
            })}
          </div>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default AlgorithmVisualizer;
