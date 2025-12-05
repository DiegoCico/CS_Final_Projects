"use client";

import { useParams } from "next/navigation";
import Link from "next/link";
import { Form, Row, Col, Card, Button } from "react-bootstrap";
import * as db from "../../../../Database";

type Assignment = {
  _id: string;
  course: string;
  title: string;
  description?: string;
  points?: number;
  group?: "assignments" | "quizzes" | "exams" | "projects";
  submissionType?: "online" | "on-paper" | "external-tool";
  dueDate?: string;
  due?: string;
  dueTime?: string;
  availableFrom?: string;
  availableFromTime?: string;
  availableUntil?: string;
  availableUntilTime?: string;
};

export default function AssignmentEditor() {
  const { cid: courseId, aid: assignmentId } = useParams<{
    cid: string;
    aid: string;
  }>();

  const assignments = (db as { assignments?: Assignment[] }).assignments ?? [];

  const assignment = assignments.find(
    (a) => a._id === assignmentId && a.course === courseId
  );

  if (!assignment) {
    return <div className="text-danger">Assignment not found.</div>;
  }

  return (
    <div id="wd-assignments-editor" className="w-100">
      <Form>
        <Form.Group controlId="wd-name" className="mb-3">
          <Form.Label className="fw-semibold">Assignment Name</Form.Label>
          <Form.Control defaultValue={assignment.title} />
        </Form.Group>

        <Form.Group controlId="wd-description" className="mb-4">
          <Form.Label className="fw-semibold">Description</Form.Label>
          <Form.Control
            as="textarea"
            rows={7}
            defaultValue={assignment.description ?? ""}
          />
        </Form.Group>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label htmlFor="wd-points" className="mt-2 fw-semibold">
              Points
            </Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              id="wd-points"
              type="number"
              defaultValue={assignment.points ?? 100}
              style={{ maxWidth: 160 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label htmlFor="wd-group" className="mt-2 fw-semibold">
              Assignment Group
            </Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Select
              id="wd-group"
              defaultValue={assignment.group ?? "assignments"}
              style={{ maxWidth: 320 }}
            >
              <option value="assignments">ASSIGNMENTS</option>
              <option value="quizzes">QUIZZES</option>
              <option value="exams">EXAMS</option>
              <option value="projects">PROJECTS</option>
            </Form.Select>
          </Col>
        </Row>

        <Row className="mb-4">
          <Col sm={3} className="text-sm-end">
            <Form.Label htmlFor="wd-submission-type" className="mt-2 fw-semibold">
              Submission Type
            </Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Select
              id="wd-submission-type"
              defaultValue={assignment.submissionType ?? "online"}
              style={{ maxWidth: 320 }}
              className="mb-3"
            >
              <option value="online">Online</option>
              <option value="on-paper">On Paper</option>
              <option value="external-tool">External Tool</option>
            </Form.Select>

            <Card className="border-1">
              <Card.Body>
                <div className="fw-semibold mb-2">Online Entry Options</div>
                <div className="d-flex flex-column gap-2">
                  <Form.Check id="wd-entry-text" type="checkbox" label="Text Entry" />
                  <Form.Check id="wd-entry-url" type="checkbox" label="Website URL" defaultChecked />
                  <Form.Check id="wd-entry-media" type="checkbox" label="Media Recordings" />
                  <Form.Check id="wd-entry-annotation" type="checkbox" label="Student Annotation" />
                  <Form.Check id="wd-entry-files" type="checkbox" label="File Uploads" />
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label htmlFor="wd-due-date" className="mt-2 fw-semibold">
              Due
            </Form.Label>
          </Col>
          <Col sm={9} className="d-flex gap-2">
            <Form.Control
              id="wd-due-date"
              type="date"
              defaultValue={(assignment.dueDate ?? assignment.due ?? "").slice(0, 10)}
              style={{ maxWidth: 200 }}
            />
            <Form.Control
              id="wd-due-time"
              type="time"
              defaultValue={assignment.dueTime ?? "23:59"}
              style={{ maxWidth: 160 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label htmlFor="wd-available-from" className="mt-2 fw-semibold">
              Available from
            </Form.Label>
          </Col>
          <Col sm={9} className="d-flex gap-2">
            <Form.Control
              id="wd-available-from"
              type="date"
              defaultValue={(assignment.availableFrom ?? "").slice(0, 10)}
              style={{ maxWidth: 200 }}
            />
            <Form.Control
              id="wd-available-from-time"
              type="time"
              defaultValue={assignment.availableFromTime ?? "00:00"}
              style={{ maxWidth: 160 }}
            />
          </Col>
        </Row>

        <Row className="mb-4">
          <Col sm={3} className="text-sm-end">
            <Form.Label htmlFor="wd-available-until" className="mt-2 fw-semibold">
              Until
            </Form.Label>
          </Col>
          <Col sm={9} className="d-flex gap-2">
            <Form.Control
              id="wd-available-until"
              type="date"
              defaultValue={(assignment.availableUntil ?? "").slice(0, 10)}
              style={{ maxWidth: 200 }}
            />
            <Form.Control
              id="wd-available-until-time"
              type="time"
              defaultValue={assignment.availableUntilTime ?? "00:00"}
              style={{ maxWidth: 160 }}
            />
          </Col>
        </Row>

        <div className="d-flex justify-content-end gap-2">
  <Link
    href={`/Kambaz/Courses/${courseId}/Assignments`}
    className="btn btn-secondary"
  >
    Cancel
  </Link>
  <Link
    href={`/Kambaz/Courses/${courseId}/Assignments`}
    className="btn btn-danger"
  >
    Save
  </Link>
</div>

      </Form>
    </div>
  );
}
