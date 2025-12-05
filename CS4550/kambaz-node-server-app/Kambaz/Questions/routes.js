import QuestionsDao from "./dao.js";
import QuestionsModel from "./model.js";
import QuizModel from "../Quizzes/model.js";

export default function QuestionsRoutes(app) {
  const dao = QuestionsDao();

  // ----------------------------------------------------
  // Helper: Remove correct answers for students
  // ----------------------------------------------------
  function sanitizeQuestion(q) {
    const clean = { ...q._doc };

    // Remove MCQ correctness
    if (clean.choices) {
      clean.choices = clean.choices.map((c) => ({
        _id: c._id,
        text: c.text
      }));
    }

    delete clean.correctAnswer;   // TRUE/FALSE
    delete clean.possibleAnswers; // FILL-IN
    delete clean.caseSensitive;

    return clean;
  }

  // ----------------------------------------------------
  // Helper: Enforce question visibility rules
  // ----------------------------------------------------
  async function filterQuestionsForStudent(quiz, questions) {
    const now = new Date();

    // 1. Quiz must be published
    if (!quiz.published) return [];

    // 2. Quiz must be within availability window
    if (quiz.availableDate && now < quiz.availableDate) return [];
    if (quiz.untilDate && now > quiz.untilDate) return [];

    // 3. One-question-at-a-time -> students CANNOT fetch all questions
    if (quiz.oneQuestionAtATime) return null; // special signal

    // 4. Sanitize correct answers unless allowed
    if (quiz.showCorrectAnswers === "IMMEDIATELY") {
      return questions;
    }

    if (
      quiz.showCorrectAnswers === "AFTER_DUE_DATE" &&
      quiz.dueDate &&
      now > quiz.dueDate
    ) {
      return questions;
    }

    return questions.map((q) => sanitizeQuestion(q));
  }

  // ----------------------------------------------------
  // Helper: Recalculate total quiz points
  // ----------------------------------------------------
  async function updateQuizTotalPoints(quizId) {
    const questions = await QuestionsModel.find({ quiz: quizId });
    const total = questions.reduce((sum, q) => sum + (q.points || 0), 0);

    await QuizModel.updateOne(
      { _id: quizId },
      { $set: { points: total } }
    );
  }

  // ----------------------------------------------------
  // GET all questions for a quiz
  // ----------------------------------------------------
  const findQuestionsForQuiz = async (req, res) => {
    const { quizId } = req.params;
    const currentUser = req.session?.currentUser;

    const quiz = await QuizModel.findById(quizId);
    if (!quiz) return res.status(404).json({ message: "Quiz not found" });

    const questions = await dao.findQuestionsForQuiz(quizId);

    // Faculty always gets full questions
    if (!currentUser || currentUser.role === "FACULTY") {
      return res.json(questions);
    }

    // STUDENT LOGIC
    const filtered = await filterQuestionsForStudent(quiz, questions);

    // One-question-at-a-time → disallow list fetch
    if (filtered === null) {
      return res.status(403).json({
        code: "ONE_QUESTION_AT_A_TIME",
        message: "You cannot view all questions at once."
      });
    }

    return res.json(filtered);
  };

  // ----------------------------------------------------
  // GET a single question (still must follow rules)
  // ----------------------------------------------------
  const findQuestionById = async (req, res) => {
    const { questionId } = req.params;
    const question = await dao.findQuestionById(questionId);

    if (!question) {
      return res.status(404).json({ message: "Question not found" });
    }

    const quiz = await QuizModel.findById(question.quiz);
    const currentUser = req.session?.currentUser;

    // Faculty gets full detail
    if (!currentUser || currentUser.role === "FACULTY") {
      return res.json(question);
    }

    // STUDENT visibility restrictions
    const filtered = await filterQuestionsForStudent(quiz, [question]);

    if (filtered === null) {
      return res.status(403).json({
        code: "ONE_QUESTION_AT_A_TIME",
        message: "You cannot view this question directly."
      });
    }

    if (filtered.length === 0) {
      return res.status(403).json({
        code: "NOT_AVAILABLE",
        message: "You are not allowed to view this question."
      });
    }

    return res.json(filtered[0]);
  };

  // ----------------------------------------------------
  // CREATE a question
  // ----------------------------------------------------
  const createQuestionForQuiz = async (req, res) => {
    const { quizId } = req.params;
    const question = { ...req.body, quiz: quizId };

    const newQuestion = await dao.createQuestion(question);

    await updateQuizTotalPoints(quizId);

    res.json(newQuestion);
  };

  // ----------------------------------------------------
  // UPDATE a question
  // ----------------------------------------------------
  const updateQuestion = async (req, res) => {
    const { questionId } = req.params;
    const updates = req.body;

    const existing = await dao.findQuestionById(questionId);
    if (!existing) {
      return res.status(404).json({ message: "Question not found" });
    }

    const status = await dao.updateQuestion(questionId, updates);

    await updateQuizTotalPoints(existing.quiz);

    res.json(status);
  };

  // ----------------------------------------------------
  // DELETE a question
  // ----------------------------------------------------
  const deleteQuestion = async (req, res) => {
    const { questionId } = req.params;

    const existing = await dao.findQuestionById(questionId);
    if (!existing) {
      return res.status(404).json({ message: "Question not found" });
    }

    const status = await dao.deleteQuestion(questionId);

    await updateQuizTotalPoints(existing.quiz);

    res.json(status);
  };

  // ----------------------------------------------------
  // ROUTES
  // ----------------------------------------------------
  app.get("/api/quizzes/:quizId/questions", findQuestionsForQuiz);
  app.post("/api/quizzes/:quizId/questions", createQuestionForQuiz);
  app.get("/api/questions/:questionId", findQuestionById);
  app.put("/api/questions/:questionId", updateQuestion);
  app.delete("/api/questions/:questionId", deleteQuestion);
}
