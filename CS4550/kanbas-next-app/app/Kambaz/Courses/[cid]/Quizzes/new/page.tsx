"use client";

import { useState } from "react";
import { useRouter, useParams } from "next/navigation";
import { createQuizForCourse } from "../../../client";
import { useSelector } from "react-redux";
import { Form, Row, Col, Button } from "react-bootstrap";

export default function NewQuizPage() {
  const router = useRouter();
  const { cid } = useParams();

  const currentUser = useSelector((state: any) => state.account.currentUser);
  if (!currentUser || currentUser.role !== "FACULTY") {
    return <div className="p-4 text-danger">Unauthorized - Faculty only</div>;
  }

  const [quiz, setQuiz] = useState({
    title: "",
    description: "",
    published: false,
    multipleAttempts: false,
    howManyAttempts: 1,
    timeLimit: 0,
    availableDate: "",
    untilDate: "",
    dueDate: "",
    accessCode: "",
    oneQuestionAtATime: false,
    lockQuestionsAfterAnswering: false,
    showCorrectAnswers: "NEVER",
  });

  const handleChange = (field: string, value: any) => {
    setQuiz({ ...quiz, [field]: value });
  };

  const handleSubmit = async () => {
    await createQuizForCourse(cid as string, quiz);
    router.push(`/Kambaz/Courses/${cid}/Quizzes`);
  };

  return (
    <div id="wd-quiz-editor" className="w-100">
      <Form>
        <Form.Group className="mb-3">
          <Form.Label className="fw-semibold">Quiz Title</Form.Label>
          <Form.Control
            value={quiz.title}
            onChange={(e) => handleChange("title", e.target.value)}
            placeholder="Enter quiz title"
          />
        </Form.Group>

        <Form.Group className="mb-4">
          <Form.Label className="fw-semibold">Description</Form.Label>
          <Form.Control
            as="textarea"
            rows={5}
            value={quiz.description}
            onChange={(e) => handleChange("description", e.target.value)}
            placeholder="Enter quiz description"
          />
        </Form.Group>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Available From</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              type="datetime-local"
              value={quiz.availableDate}
              onChange={(e) => handleChange("availableDate", e.target.value)}
              style={{ maxWidth: 250 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Available Until</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              type="datetime-local"
              value={quiz.untilDate}
              onChange={(e) => handleChange("untilDate", e.target.value)}
              style={{ maxWidth: 250 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Due Date</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              type="datetime-local"
              value={quiz.dueDate}
              onChange={(e) => handleChange("dueDate", e.target.value)}
              style={{ maxWidth: 250 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Time Limit (minutes)</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              type="number"
              value={quiz.timeLimit}
              onChange={(e) => handleChange("timeLimit", parseInt(e.target.value))}
              style={{ maxWidth: 160 }}
            />
            <Form.Text className="text-muted">0 = no time limit</Form.Text>
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Access Code</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Control
              value={quiz.accessCode}
              onChange={(e) => handleChange("accessCode", e.target.value)}
              placeholder="Optional"
              style={{ maxWidth: 200 }}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Multiple Attempts</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Check
              type="checkbox"
              label="Allow multiple attempts"
              checked={quiz.multipleAttempts}
              onChange={(e) => handleChange("multipleAttempts", e.target.checked)}
            />
            {quiz.multipleAttempts && (
              <Form.Control
                type="number"
                value={quiz.howManyAttempts}
                onChange={(e) => handleChange("howManyAttempts", parseInt(e.target.value))}
                className="mt-2"
                style={{ maxWidth: 100 }}
                min={1}
              />
            )}
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Quiz Options</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Check
              type="checkbox"
              label="Show one question at a time"
              checked={quiz.oneQuestionAtATime}
              onChange={(e) => handleChange("oneQuestionAtATime", e.target.checked)}
              className="mb-2"
            />
            <Form.Check
              type="checkbox"
              label="Lock questions after answering"
              checked={quiz.lockQuestionsAfterAnswering}
              onChange={(e) => handleChange("lockQuestionsAfterAnswering", e.target.checked)}
            />
          </Col>
        </Row>

        <Row className="mb-3">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Show Correct Answers</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Select
              value={quiz.showCorrectAnswers}
              onChange={(e) => handleChange("showCorrectAnswers", e.target.value)}
              style={{ maxWidth: 300 }}
            >
              <option value="NEVER">Never</option>
              <option value="IMMEDIATELY">Immediately after submission</option>
              <option value="AFTER_DUE_DATE">After Due Date</option>
            </Form.Select>
          </Col>
        </Row>

        <Row className="mb-4">
          <Col sm={3} className="text-sm-end">
            <Form.Label className="mt-2 fw-semibold">Publish</Form.Label>
          </Col>
          <Col sm={9}>
            <Form.Check
              type="checkbox"
              label="Publish quiz immediately"
              checked={quiz.published}
              onChange={(e) => handleChange("published", e.target.checked)}
            />
          </Col>
        </Row>

        <div className="d-flex justify-content-end gap-2">
          <Button
            variant="secondary"
            onClick={() => router.push(`/Kambaz/Courses/${cid}/Quizzes`)}
          >
            Cancel
          </Button>

          <Button variant="danger" onClick={handleSubmit}>
            Create Quiz
          </Button>
        </div>
      </Form>
    </div>
  );
}
