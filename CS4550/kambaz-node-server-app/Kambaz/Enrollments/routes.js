import EnrollmentsDao from "./dao.js";

export default function EnrollmentsRoutes(app) {
  const dao = EnrollmentsDao();

  const enrollUserInCourse = async (req, res) => {
    const { uid, cid } = req.params;
    const status = await dao.enrollUserInCourse(uid, cid);
    res.json(status);
  };

  const unenrollUserFromCourse = async (req, res) => {
    const { uid, cid } = req.params;
    const status = await dao.unenrollUserFromCourse(uid, cid);
    res.json(status);
  };

  const findEnrollmentsForUser = async (req, res) => {
    const { uid } = req.params;
    const courses = await dao.findCoursesForUser(uid);
    res.json(courses);
  };

  app.post("/api/users/:uid/courses/:cid", enrollUserInCourse);
  app.delete("/api/users/:uid/courses/:cid", unenrollUserFromCourse);
  app.get("/api/users/:uid/courses", findEnrollmentsForUser);
}
