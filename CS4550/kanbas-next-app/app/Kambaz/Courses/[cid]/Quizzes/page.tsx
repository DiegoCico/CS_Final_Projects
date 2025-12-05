"use client";

import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import {
  findQuizzesForCourse,
  deleteQuiz,
  publishQuiz,
  unpublishQuiz,
} from "../../client";
import Link from "next/link";
import { useRouter, useParams } from "next/navigation";
import {
  ListGroup,
  ListGroupItem,
  InputGroup,
  Form,
  Button,
} from "react-bootstrap";
import {
  BsGripVertical,
  BsThreeDotsVertical,
  BsPlusLg,
  BsSearch,
  BsTrash,
} from "react-icons/bs";
import { FaCheckCircle, FaTimesCircle } from "react-icons/fa";
import GreenCheckmark from "../Modules/GreenCheckmark";

export default function QuizzesPage() {
  const router = useRouter();
  const { cid } = useParams();
  const currentUser = useSelector((state: any) => state.account.currentUser);

  const [quizzes, setQuizzes] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const loadQuizzes = async () => {
    try {
      const data = await findQuizzesForCourse(cid as string);
      setQuizzes(data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadQuizzes();
  }, [cid]);

  const handleDelete = async (quizId: string) => {
    if (!confirm("Are you sure you want to delete this quiz?")) return;
    await deleteQuiz(quizId);
    loadQuizzes();
  };

  const handlePublish = async (quizId: string) => {
    await publishQuiz(quizId);
    loadQuizzes();
  };

  const handleUnpublish = async (quizId: string) => {
    await unpublishQuiz(quizId);
    loadQuizzes();
  };

  if (loading) return <div className="p-4">Loading...</div>;

  const isFaculty = currentUser?.role === "FACULTY";

  return (
    <div id="wd-quizzes">
      <div className="clearfix">
        <div className="w-50 d-inline-block">
          <InputGroup>
            <InputGroup.Text className="bg-white">
              <BsSearch />
            </InputGroup.Text>
            <Form.Control
              id="wd-search-quiz"
              placeholder="Search for Quizzes"
            />
          </InputGroup>
        </div>

        {isFaculty && (
          <Button
            id="wd-add-quiz"
            variant="danger"
            size="lg"
            className="float-end"
            onClick={() => router.push(`/Kambaz/Courses/${cid}/Quizzes/new`)}
          >
            <BsPlusLg className="me-2" />
            Quiz
          </Button>
        )}
      </div>

      <br />
      <ListGroup className="rounded-0 mt-4">
        <ListGroupItem className="p-0 mb-4 fs-5 border-gray">
          <div className="p-3 ps-2 bg-secondary d-flex align-items-center justify-content-between border-start border-4 border-success">
            <div className="d-flex align-items-center">
              <BsGripVertical className="me-2 fs-3" />
              <span className="fw-bold">QUIZZES</span>
            </div>
            <div className="d-flex align-items-center">
              <GreenCheckmark />
              {isFaculty && (
                <>
                  <BsPlusLg className="me-3" />
                  <BsThreeDotsVertical />
                </>
              )}
            </div>
          </div>

          <ListGroup className="rounded-0">
            {quizzes.map((quiz) => (
              <ListGroupItem
                key={quiz._id}
                className="p-3 ps-2 d-flex align-items-start justify-content-between"
              >
                <div className="d-flex align-items-start">
                  <BsGripVertical className="me-2 fs-3 text-secondary" />
                  <div>
                    <Link
                      href={`/Kambaz/Courses/${cid}/Quizzes/${quiz._id}`}
                      className="fw-bold text-decoration-none text-dark"
                    >
                      {quiz.title}
                    </Link>
                    <div className="text-muted small">
                      {quiz.availableDate && (
                        <>
                          <b>Available:</b>{" "}
                          {new Date(quiz.availableDate).toLocaleDateString()} |{" "}
                        </>
                      )}
                      {quiz.dueDate && (
                        <>
                          <b>Due:</b>{" "}
                          {new Date(quiz.dueDate).toLocaleDateString()} |{" "}
                        </>
                      )}
                      {quiz.points} pts
                      {quiz.published ? (
                        <span className="ms-2 text-success">
                          <FaCheckCircle /> Published
                        </span>
                      ) : (
                        <span className="ms-2 text-danger">
                          <FaTimesCircle /> Unpublished
                        </span>
                      )}
                    </div>
                  </div>
                </div>

                {isFaculty && (
                  <div className="d-flex align-items-center gap-2">
                    {quiz.published ? (
                      <Button
                        variant="outline-secondary"
                        size="sm"
                        onClick={() => handleUnpublish(quiz._id)}
                      >
                        Unpublish
                      </Button>
                    ) : (
                      <Button
                        variant="outline-success"
                        size="sm"
                        onClick={() => handlePublish(quiz._id)}
                      >
                        Publish
                      </Button>
                    )}

                    <Button
                      variant="link"
                      className="text-danger p-0"
                      onClick={() => handleDelete(quiz._id)}
                    >
                      <BsTrash />
                    </Button>
                  </div>
                )}
              </ListGroupItem>
            ))}

            {quizzes.length === 0 && (
              <ListGroupItem className="text-muted">
                No quizzes for this course.
              </ListGroupItem>
            )}
          </ListGroup>
        </ListGroupItem>
      </ListGroup>
    </div>
  );
}
