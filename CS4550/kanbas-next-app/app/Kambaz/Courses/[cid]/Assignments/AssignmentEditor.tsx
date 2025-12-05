"use client";

import { useParams, useRouter } from "next/navigation";
import { useSelector, useDispatch } from "react-redux";
import { useState, useEffect } from "react";
import Link from "next/link";
import { Form, Row, Col, Button } from "react-bootstrap";

import {
  createAssignmentForCourse,
  updateAssignment,
  findAssignmentsForCourse,
} from "../../client";

import { setAssignments } from "./reducer";

export default function AssignmentEditor() {
  const { cid: courseId, aid: assignmentId } = useParams<{
    cid: string;
    aid: string;
  }>();

  const router = useRouter();
  const dispatch = useDispatch();
  const { assignments } = useSelector((state: any) => state.assignments);

  const existing = assignments.find(
    (a: any) => a._id === assignmentId && a.course === courseId
  );

  const [assignment, setAssignment] = useState(
    existing || {
      course: courseId,
      title: "",
      description: "",
      points: 100,
      due: "",
      availableFrom: "",
      availableUntil: "",
    }
  );

  useEffect(() => {
    if (!existing && assignmentId !== "new") {
      findAssignmentsForCourse(courseId).then((data) => {
        dispatch(setAssignments(data));

        const found = data.find(
          (a: any) => a._id === assignmentId && a.course === courseId
        );

        if (found) {
          setAssignment(found);
        }
      });
    }
  }, []);

  const handleChange = (field: string, value: any) => {
    setAssignment({ ...assignment, [field]: value });
  };

  const handleSave = async () => {
    if (existing) {
      await updateAssignment(assignment);
    } else {
      await createAssignmentForCourse(courseId, assignment);
    }

    const updated = await findAssignmentsForCourse(courseId);
    dispatch(setAssignments(updated));

    router.push(`/Kambaz/Courses/${courseId}/Assignments`);
  };

  return (
    <div id="wd-assignments-editor" className="w-100">
      <Form>

        <Form.Group className="mb-3">
          <Form.Label className="fw-semibold">Assignment Name</Form.Label>
          <Form.Control
            value={assignment.title}
            onChange={(e) => handleChange("title", e.target.value)}
          />
        </Form.Group>

        <Form.Group className="mb-4">
          <Form.Label className="fw-semibold">Description</Form.Label>
          <Form.Control
            as="textarea"
            rows={5}
            value={assignment.description}
            onChange={(e) => handleChange("description", e.target.value)}
          />
        </Form.Group>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Points</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              type="number"
              value={assignment.points}
              onChange={(e) => handleChange("points", e.target.value)}
              style={{ maxWidth: 160 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Due</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              type="date"
              value={assignment.due}
              onChange={(e) => handleChange("due", e.target.value)}
              style={{ maxWidth: 200 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Available From</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              type="date"
              value={assignment.availableFrom}
              onChange={(e) =>
                handleChange("availableFrom", e.target.value)
              }
              style={{ maxWidth: 200 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Until</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              type="date"
              value={assignment.availableUntil}
              onChange={(e) =>
                handleChange("availableUntil", e.target.value)
              }
              style={{ maxWidth: 200 }}
            />
          </Col>
        </Row>

        <div className="d-flex justify-content-end gap-2">
          <Button
            variant="secondary"
            onClick={() =>
              router.push(`/Kambaz/Courses/${courseId}/Assignments`)
            }
          >
            Cancel
          </Button>

          <Button variant="danger" onClick={handleSave}>
            Save
          </Button>
        </div>

      </Form>
    </div>
  );
}
