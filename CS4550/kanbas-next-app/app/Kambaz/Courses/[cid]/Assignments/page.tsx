"use client";
import Link from "next/link";
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
import { useParams, useRouter } from "next/navigation";
import { useSelector, useDispatch } from "react-redux";

import {
  findAssignmentsForCourse,
  deleteAssignment as deleteAssignmentAPI,
} from "../../client";

import { setAssignments, deleteAssignment } from "./reducer";
import GreenCheckmark from "../Modules/GreenCheckmark";
import { useEffect } from "react";

export default function Assignments() {
  const { cid } = useParams();
  const router = useRouter();
  const dispatch = useDispatch();

  const courseId = Array.isArray(cid) ? cid[0] : cid;
  const { assignments } = useSelector((state: any) => state.assignments);

  // Load from SERVER on page load
  useEffect(() => {
    if (courseId) {
      findAssignmentsForCourse(courseId).then((data) => {
        dispatch(setAssignments(data));
      });
    }
  }, [courseId, dispatch]);

  const courseAssignments = assignments.filter(
    (a: any) => a.course === courseId
  );

  const handleDelete = async (id: string) => {
    if (!confirm("Are you sure you want to delete this assignment?")) return;

    await deleteAssignmentAPI(id);
    dispatch(deleteAssignment(id));
  };

  return (
    <div id="wd-assignments">
      <div className="clearfix">
        <div className="w-50 d-inline-block">
          <InputGroup>
            <InputGroup.Text className="bg-white">
              <BsSearch />
            </InputGroup.Text>
            <Form.Control
              id="wd-search-assignment"
              placeholder="Search for Assignments"
            />
          </InputGroup>
        </div>

        <Button
          id="wd-add-assignment"
          variant="danger"
          size="lg"
          className="me-1 float-end"
          onClick={() =>
            router.push(`/Kambaz/Courses/${courseId}/Assignments/new`)
          }
        >
          <BsPlusLg className="me-2" />
          Assignment
        </Button>
        <Button
          id="wd-add-assignment-group"
          variant="secondary"
          size="lg"
          className="float-end me-2"
        >
          <BsPlusLg className="me-2" />
          Group
        </Button>
      </div>

      <br />
      <ListGroup className="rounded-0 mt-4">
        <ListGroupItem className="p-0 mb-4 fs-5 border-gray">
          <div className="p-3 ps-2 bg-secondary d-flex align-items-center justify-content-between border-start border-4 border-success">
            <div className="d-flex align-items-center">
              <BsGripVertical className="me-2 fs-3" />
              <span className="fw-bold">ASSIGNMENTS</span>
            </div>
            <div className="d-flex align-items-center">
              <GreenCheckmark />
              <BsPlusLg className="me-3" />
              <BsThreeDotsVertical />
            </div>
          </div>

          <ListGroup className="rounded-0">
            {courseAssignments.map((a: any) => (
              <ListGroupItem
                key={a._id}
                className="p-3 ps-2 d-flex align-items-start justify-content-between"
              >
                <div className="d-flex align-items-start">
                  <BsGripVertical className="me-2 fs-3 text-secondary" />
                  <div>
                    <Link
                      href={`/Kambaz/Courses/${courseId}/Assignments/${a._id}`}
                      className="fw-bold text-decoration-underline text-primary"
                    >
                      {a.title}
                    </Link>
                    <div className="text-muted small">
                      {a.availableFrom && (
                        <>
                          <b>Not available until</b> {a.availableFrom} |{" "}
                        </>
                      )}
                      <b>Due</b> {a.due} | {a.points} pts
                    </div>
                  </div>
                </div>
                <div className="d-flex align-items-center">
                  <Button
                    variant="link"
                    className="text-danger p-0"
                    onClick={() => handleDelete(a._id)}
                  >
                    <BsTrash />
                  </Button>
                </div>
              </ListGroupItem>
            ))}

            {courseAssignments.length === 0 && (
              <ListGroupItem className="text-muted">
                No assignments for this course.
              </ListGroupItem>
            )}
          </ListGroup>
        </ListGroupItem>
      </ListGroup>
    </div>
  );
}
