import QuizzesDao from "./dao.js";

export default function QuizzesRoutes(app) {
  const dao = QuizzesDao();

  const requireAuth = (req, res, next) => {
    const currentUser = req.session["currentUser"];
    if (!currentUser) {
      return res.status(401).json({ message: "Unauthorized - Please sign in" });
    }
    next();
  };

  const requireFaculty = (req, res, next) => {
    const currentUser = req.session["currentUser"];
    if (!currentUser) {
      return res.status(401).json({ message: "Unauthorized - Please sign in" });
    }
    if (currentUser.role !== "FACULTY") {
      return res.status(403).json({ message: "Forbidden - Faculty access required" });
    }
    next();
  };

  // ---------------------------------------------------------
  // Canvas-style visibility filter for STUDENTS
  // ---------------------------------------------------------
  function filterQuizzesForStudent(quizList) {
    const now = new Date();

    return quizList.filter(q => {
      // Must be published
      if (!q.published) return false;

      // Must be past availableDate (if set)
      if (q.availableDate && now < q.availableDate) return false;

      // Must not be past untilDate (if set)
      if (q.untilDate && now > q.untilDate) return false;

      return true;
    });
  }

  // ---------------------------------------------------------
  // GET all quizzes for a course
  // ---------------------------------------------------------
  const findQuizzesForCourse = async (req, res) => {
    const { courseId } = req.params;
    const currentUser = req.session["currentUser"];

    let quizzes = await dao.findQuizzesForCourse(courseId);

    // Faculty sees everything
    if (currentUser?.role === "FACULTY") {
      return res.json(quizzes);
    }

    // Students require full Canvas visibility filtering
    quizzes = filterQuizzesForStudent(quizzes);

    res.json(quizzes);
  };

  // ---------------------------------------------------------
  // GET one quiz by ID
  // Students must not access hidden or unavailable quizzes
  // ---------------------------------------------------------
  const findQuizById = async (req, res) => {
    const { quizId } = req.params;
    const currentUser = req.session["currentUser"];
    const quiz = await dao.findQuizById(quizId);

    if (!quiz) {
      return res.status(404).json({ message: "Quiz not found" });
    }

    // Faculty always has access
    if (currentUser?.role === "FACULTY") {
      return res.json(quiz);
    }

    // Students must pass ALL visibility rules
    const now = new Date();

    if (!quiz.published) {
      return res.status(403).json({
        message: "This quiz is not published",
        code: "QUIZ_UNAVAILABLE"
      });
    }

    if (quiz.availableDate && now < quiz.availableDate) {
      return res.status(403).json({
        message: `Quiz is not available until ${quiz.availableDate}`,
        code: "NOT_AVAILABLE_YET"
      });
    }

    if (quiz.untilDate && now > quiz.untilDate) {
      return res.status(403).json({
        message: `Quiz closed on ${quiz.untilDate}`,
        code: "QUIZ_CLOSED"
      });
    }

    return res.json(quiz);
  };

  // ---------------------------------------------------------
  // Faculty-only CRUD operations
  // ---------------------------------------------------------
  const createQuizForCourse = async (req, res) => {
    const { courseId } = req.params;
    const quiz = { ...req.body, course: courseId };
    const newQuiz = await dao.createQuiz(quiz);
    res.json(newQuiz);
  };

  const updateQuiz = async (req, res) => {
    const { quizId } = req.params;
    const updates = req.body;
    const status = await dao.updateQuiz(quizId, updates);
    res.json(status);
  };

  const deleteQuiz = async (req, res) => {
    const { quizId } = req.params;
    const status = await dao.deleteQuiz(quizId);
    res.json(status);
  };

  const publishQuiz = async (req, res) => {
    const { quizId } = req.params;
    const status = await dao.publishQuiz(quizId);
    res.json(status);
  };

  const unpublishQuiz = async (req, res) => {
    const { quizId } = req.params;
    const status = await dao.unpublishQuiz(quizId);
    res.json(status);
  };

  // ---------------------------------------------------------
  // ROUTES
  // ---------------------------------------------------------
  app.get("/api/courses/:courseId/quizzes", requireAuth, findQuizzesForCourse);
  app.get("/api/quizzes/:quizId", requireAuth, findQuizById);

  app.post("/api/courses/:courseId/quizzes", requireFaculty, createQuizForCourse);
  app.put("/api/quizzes/:quizId", requireFaculty, updateQuiz);
  app.delete("/api/quizzes/:quizId", requireFaculty, deleteQuiz);
  app.put("/api/quizzes/:quizId/publish", requireFaculty, publishQuiz);
  app.put("/api/quizzes/:quizId/unpublish", requireFaculty, unpublishQuiz);
}
