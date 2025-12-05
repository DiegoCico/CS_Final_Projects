import model from "./model.js";
import { v4 as uuidv4 } from "uuid";

export default function AssignmentsDao() {
  const createAssignment = (assignment) => {
    const newAssignment = { ...assignment, _id: uuidv4() };
    return model.create(newAssignment);
  };

  const deleteAssignment = (assignmentId) =>
    model.deleteOne({ _id: assignmentId });

  const updateAssignment = (assignmentId, updates) =>
    model.updateOne({ _id: assignmentId }, { $set: updates });

  const findAssignmentsForCourse = (courseId) =>
    model.find({ course: courseId });

  return {
    findAssignmentsForCourse,
    createAssignment,
    deleteAssignment,
    updateAssignment,
  };
}
