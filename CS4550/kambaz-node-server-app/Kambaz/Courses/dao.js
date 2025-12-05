import { v4 as uuidv4 } from "uuid";
import model from "./model.js";
import EnrollmentModel from "../Enrollments/model.js";

export default function CoursesDao() {
  function findAllCourses() {
    return model.find({}, { name: 1, description: 1 });
  }

  async function findCoursesForEnrolledUser(userId) {
    const { enrollments } = db;
    const courses = await model.find({}, { name: 1, description: 1 });
    const enrolledCourses = courses.filter((course) =>
      enrollments.some(
        (enrollment) =>
          enrollment.user === userId && enrollment.course === course._id
      )
    );
    return enrolledCourses;
    }


  async function createCourse(course) {
    const newCourse = { ...course, _id: uuidv4() };
    return model.create(newCourse);
  }

  function deleteCourse(courseId) {
    return model.deleteOne({ _id: courseId });
  }

  function updateCourse(courseId, updates) {
    return model.updateOne({ _id: courseId }, { $set: updates });
  }

  return {
    findAllCourses,
    findCoursesForEnrolledUser,
    createCourse,
    deleteCourse,
    updateCourse,
  };
}
