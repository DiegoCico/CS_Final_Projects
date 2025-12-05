import QuizAttemptsDao from "./dao.js";
import QuestionsModel from "../Questions/model.js";
import QuizModel from "../Quizzes/model.js";
import model from "./model.js";

export default function QuizAttemptsRoutes(app) {
  const dao = QuizAttemptsDao();

  // ----------------------------------------------------
  // Return ALL attempts for the current user
  // ----------------------------------------------------
const findAttemptsForQuiz = async (req, res) => {
  const { quizId } = req.params;
  const sessionUser = req.session?.currentUser;

  if (!sessionUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  const attempts = await dao.findAttemptsForQuiz(quizId, sessionUser._id);
  res.json(attempts);
};


  function sanitizeQuestionsForStudent(quiz, questions) {
  const now = new Date();

  // Faculty always gets full data
  // This method is only called for students
  if (quiz.showCorrectAnswers === "IMMEDIATELY") {
    return questions; // no filtering
  }

  if (quiz.showCorrectAnswers === "AFTER_DUE_DATE") {
    if (quiz.dueDate && now > quiz.dueDate) {
      return questions; // due date passed, show correct answers
    }
  }

  // NEVER show correct answers (or until due date not reached)
  return questions.map(q => {
    const clean = { ...q._doc }; // clone document safely

    if (clean.choices) {
      clean.choices = clean.choices.map(c => ({
        _id: c._id,
        text: c.text,
        // REMOVE isCorrect flag
      }));
    }

    delete clean.correctAnswer;       // True/False correct value
    delete clean.possibleAnswers;     // Fill-in answers
    delete clean.caseSensitive;       

    return clean;
  });
}


  // ----------------------------------------------------
  // Return latest attempt for the current user
  // ----------------------------------------------------
 const findLatestAttempt = async (req, res) => {
  const { quizId } = req.params;
  const sessionUser = req.session?.currentUser;

  if (!sessionUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  const attempt = await dao.findLatestAttempt(quizId, sessionUser._id);
  if (!attempt) {
    return res.status(404).json({ message: "No attempts found" });
  }

  res.json(attempt);
};


  // ----------------------------------------------------
  // AUTOGRADING LOGIC
  // ----------------------------------------------------
  function gradeAttempt(answers, questions) {
    let totalScore = 0;
    let totalPoints = 0;

    const gradedAnswers = answers.map((ans) => {
      const q = questions.find((qq) => qq._id === ans.question);
      if (!q) return { ...ans, isCorrect: false, pointsEarned: 0 };

      totalPoints += q.points;
      let isCorrect = false;

      if (q.type === "MULTIPLE_CHOICE") {
        const correctChoice = q.choices.find((c) => c.isCorrect === true);
        isCorrect = ans.answer === correctChoice?._id;
      }

      else if (q.type === "TRUE_FALSE") {
        isCorrect = Boolean(ans.answer) === q.correctAnswer;
      }

      else if (q.type === "FILL_IN_BLANK") {
        const student = ans.answer.toString().trim();
        isCorrect = q.possibleAnswers.some((correct) => {
          return q.caseSensitive
            ? student === correct
            : student.toLowerCase() === correct.toLowerCase();
        });
      }

      const pointsEarned = isCorrect ? q.points : 0;
      totalScore += pointsEarned;

      return { ...ans, isCorrect, pointsEarned };
    });

    return { gradedAnswers, totalScore, totalPoints };
  }

  // ----------------------------------------------------
  // CREATE ATTEMPT WITH:
  // - attempt limit enforcement
  // - date availability enforcement
  // - autograding
  // ----------------------------------------------------
const createAttempt = async (req, res) => {
  const { quizId } = req.params;
  const currentUser = req.session?.currentUser;

  // Must be logged in
  if (!currentUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  const userId = currentUser._id;
  const { attemptId, answers, accessCode } = req.body;

  // ---------------------------------------------
  // LOAD QUIZ
  // ---------------------------------------------
  const quiz = await QuizModel.findById(quizId);
  if (!quiz) {
    return res.status(404).json({ message: "Quiz not found" });
  }

  // ---------------------------------------------
  // LOAD ATTEMPT (required for time-limit)
  // ---------------------------------------------
  const attempt = await dao.findAttemptById(attemptId);
  if (!attempt) {
    return res.status(404).json({ message: "Attempt not found" });
  }

  // ---------------------------------------------
  // ACCESS CODE CHECK (students only)
  // ---------------------------------------------
  if (quiz.accessCode && currentUser.role === "STUDENT") {
    if (!accessCode || accessCode !== quiz.accessCode) {
      return res.status(403).json({
        message: "Invalid or missing access code",
        code: "INVALID_ACCESS_CODE"
      });
    }
  }

  // ---------------------------------------------
  // ATTEMPT LIMIT CHECK
  // ---------------------------------------------
  const attemptCount = await dao.countAttempts(quizId, userId);

  // Students get **one attempt** unless multipleAttempts = true
  if (!quiz.multipleAttempts && attemptCount >= 1) {
    return res.status(403).json({
      message: "No more attempts allowed",
      code: "ATTEMPTS_EXCEEDED"
    });
  }

  // For multiple attempts, enforce howManyAttempts
  if (quiz.multipleAttempts && attemptCount >= quiz.howManyAttempts) {
    return res.status(403).json({
      message: "Attempt limit reached",
      code: "ATTEMPTS_EXCEEDED"
    });
  }

  // ---------------------------------------------
  // AVAILABILITY WINDOW CHECK
  // ---------------------------------------------
  const now = new Date();

  if (quiz.availableDate && now < quiz.availableDate) {
    return res.status(403).json({
      message: `Quiz is not available until ${quiz.availableDate}`,
      code: "NOT_AVAILABLE_YET"
    });
  }

  if (quiz.untilDate && now > quiz.untilDate) {
    return res.status(403).json({
      message: `Quiz is closed (was available until ${quiz.untilDate})`,
      code: "QUIZ_CLOSED"
    });
  }

  // ---------------------------------------------
  // TIME LIMIT CHECK
  // ---------------------------------------------
  if (quiz.timeLimit && quiz.timeLimit > 0) {
    const allowedMs = quiz.timeLimit * 60 * 1000;
    const deadline = new Date(attempt.startedAt.getTime() + allowedMs);

    if (now > deadline) {
      return res.status(403).json({
        message: "Time limit exceeded",
        code: "TIME_EXPIRED"
      });
    }
  }

  // ---------------------------------------------
  // LOAD QUESTIONS + AUTOGRADE
  // ---------------------------------------------
  const questions = await QuestionsModel.find({ quiz: quizId });

  const {
    gradedAnswers,
    totalScore,
    totalPoints
  } = gradeAttempt(answers, questions);

  // ---------------------------------------------
  // FINALIZE ATTEMPT
  // ---------------------------------------------
  attempt.answers = gradedAnswers;
  attempt.score = totalScore;
  attempt.totalPoints = totalPoints;
  attempt.submittedAt = new Date();

  await attempt.save();

  // ---------------------------------------------
  // RETURN SUBMISSION RESULTS
  // ---------------------------------------------
  res.json({
    message: "Attempt submitted successfully",
    attempt
  });
};



  const previewQuiz = async (req, res) => {
  const { quizId } = req.params;
  const currentUser = req.session?.currentUser;

  // Must be logged in
  if (!currentUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  // Must be faculty
  if (currentUser.role !== "FACULTY") {
    return res.status(403).json({
      message: "Only faculty can preview quizzes",
      code: "FACULTY_ONLY"
    });
  }

  // Load quiz
  const quiz = await QuizModel.findById(quizId);
  if (!quiz) {
    return res.status(404).json({ message: "Quiz not found" });
  }

  // Load questions
  const questions = await QuestionsModel.find({ quiz: quizId });

  // Grade exactly like a real attempt
  const { gradedAnswers, totalScore, totalPoints } = gradeAttempt(
    req.body.answers,
    questions
  );

  // RETURN ONLY — DO NOT SAVE TO DATABASE
  res.json({
    preview: true,
    score: totalScore,
    totalPoints,
    answers: gradedAnswers,
    questions
  });
};

const getNextQuestion = async (req, res) => {
  const { quizId, attemptId } = req.params;
  const userId = req.session?.currentUser?._id;

  if (!userId) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  const quiz = await QuizModel.findById(quizId);
  if (!quiz) {
    return res.status(404).json({ message: "Quiz not found" });
  }

  // If quiz is NOT one-question-at-a-time, return all questions
  if (!quiz.oneQuestionAtATime) {
    const questions = await QuestionsModel.find({ quiz: quizId }).sort({ order: 1 });
    return res.json({ mode: "FULL", questions });
  }

  const attempt = await dao.findAttemptById(attemptId);
  if (!attempt) {
    return res.status(404).json({ message: "Attempt not found" });
  }

  const questions = await QuestionsModel.find({ quiz: quizId }).sort({ order: 1 });

  // Define progress index
  const currentIndex = attempt.answers.length;

  if (currentIndex >= questions.length) {
    return res.json({
      done: true,
      message: "Quiz complete"
    });
  }

  res.json({
    done: false,
    index: currentIndex,
    question: questions[currentIndex]
  });
};

const answerOneQuestion = async (req, res) => {
  const { quizId, attemptId } = req.params;
  const sessionUser = req.session?.currentUser;
  const { questionId, answer } = req.body;

  if (!sessionUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  const quiz = await QuizModel.findById(quizId);
  if (!quiz) {
    return res.status(404).json({ message: "Quiz not found" });
  }

  const attempt = await dao.findAttemptById(attemptId);
  if (!attempt) {
    return res.status(404).json({ message: "Attempt not found" });
  }

  // ---------------------------
  // TIME LIMIT CHECK
  // ---------------------------
  if (quiz.timeLimit && quiz.timeLimit > 0) {
    const msLimit = quiz.timeLimit * 60 * 1000;
    const deadline = new Date(attempt.startedAt.getTime() + msLimit);

    if (new Date() > deadline) {
      return res.status(403).json({
        message: "Time limit exceeded",
        code: "TIME_EXPIRED",
        done: true
      });
    }
  }

  const questions = await QuestionsModel.find({ quiz: quizId }).sort({ order: 1 });
  const currentIndex = attempt.answers.length;
  const currentQuestion = questions[currentIndex];

  if (!currentQuestion) {
    return res.status(400).json({ message: "Quiz already completed" });
  }

  // ---------------------------
  // LOCK QUESTIONS AFTER ANSWERING
  // ---------------------------
  if (quiz.lockQuestionsAfterAnswering) {
    const alreadyAnswered = attempt.answers.find(a => a.question === questionId);
    if (alreadyAnswered) {
      return res.status(403).json({
        message: "This question is locked and cannot be changed",
        code: "QUESTION_LOCKED"
      });
    }
  }

  // ---------------------------
  // ENFORCE QUESTION ORDER
  // ---------------------------
  if (currentQuestion._id.toString() !== questionId) {
    return res.status(403).json({
      message: "Cannot answer out-of-order question",
      expected: currentQuestion._id,
      received: questionId
    });
  }

  // ---------------------------
  // GRADE ONE ANSWER
  // ---------------------------
  const { gradedAnswers } = gradeAttempt(
    [{ question: questionId, answer }],
    [currentQuestion]
  );

  attempt.answers.push(gradedAnswers[0]);
  await attempt.save();

  // Return next question or complete
  const nextIndex = attempt.answers.length;

  if (nextIndex >= questions.length) {
    return res.json({
      done: true,
      message: "Quiz complete"
    });
  }

  res.json({
    done: false,
    index: nextIndex,
    question: questions[nextIndex]
  });
};

const startAttempt = async (req, res) => {
  const { quizId } = req.params;
  const currentUser = req.session?.currentUser;

  if (!currentUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  const quiz = await QuizModel.findById(quizId);
  if (!quiz) {
    return res.status(404).json({ message: "Quiz not found" });
  }

  // Check access code if required (for students)
  if (quiz.accessCode && currentUser.role === "STUDENT") {
    const { accessCode } = req.body;
    if (!accessCode || accessCode !== quiz.accessCode) {
      return res.status(403).json({
        message: "Invalid or missing access code",
        code: "INVALID_ACCESS_CODE"
      });
    }
  }

  const attemptCount = await dao.countAttempts(quizId, currentUser._id);

  // Attempt limit logic (reuse from createAttempt)
  if (!quiz.multipleAttempts && attemptCount >= 1) {
    return res.status(403).json({ message: "No more attempts allowed" });
  }

  if (quiz.multipleAttempts && attemptCount >= quiz.howManyAttempts) {
    return res.status(403).json({ message: "Attempt limit reached" });
  }

  // Create the attempt
  const attempt = await dao.createAttempt({
    quiz: quizId,
    user: currentUser._id,
    answers: [],
    score: 0,
    totalPoints: 0,
    attemptNumber: attemptCount + 1,
    startedAt: new Date()
  });

  res.json(attempt);
};

const getAttemptById = async (req, res) => {
  const { quizId, attemptId } = req.params;
  const currentUser = req.session?.currentUser;

  if (!currentUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  // Load quiz
  const quiz = await QuizModel.findById(quizId);
  if (!quiz) {
    return res.status(404).json({ message: "Quiz not found" });
  }

  // Load attempt
  const attempt = await dao.findAttemptById(attemptId);
  if (!attempt) {
    return res.status(404).json({ message: "Attempt not found" });
  }

  // STUDENT cannot view someone else's attempt
  if (currentUser.role === "STUDENT" && attempt.user !== currentUser._id) {
    return res.status(403).json({
      message: "You cannot view another student's attempt",
      code: "FORBIDDEN"
    });
  }

  // Load questions
  const questions = await QuestionsModel.find({ quiz: quizId });

  // Determine if we show correct answers
  const now = new Date();
  let returnedQuestions = questions;

  if (currentUser.role === "STUDENT") {
    if (quiz.showCorrectAnswers === "NEVER") {
      returnedQuestions = questions.map(sanitizeQuestionForStudent);
    }

    if (quiz.showCorrectAnswers === "AFTER_DUE_DATE") {
      if (!quiz.dueDate || now < quiz.dueDate) {
        returnedQuestions = questions.map(sanitizeQuestionForStudent);
      }
    }

    // IMMEDIATELY -> full data is fine
  }

  return res.json({
    attempt,
    questions: returnedQuestions
  });
};

// helper used above
function sanitizeQuestionForStudent(q) {
  const clean = { ...q._doc };

  if (clean.choices) {
    clean.choices = clean.choices.map((c) => ({
      _id: c._id,
      text: c.text
    }));
  }

  delete clean.correctAnswer;
  delete clean.possibleAnswers;
  delete clean.caseSensitive;

  return clean;
}

const getAllAttemptsForQuiz = async (req, res) => {
  const { quizId } = req.params;
  const currentUser = req.session?.currentUser;

  // Must be logged in
  if (!currentUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  // Only faculty can access this
  if (currentUser.role !== "FACULTY") {
    return res.status(403).json({
      message: "Only faculty can view all attempts",
      code: "FACULTY_ONLY"
    });
  }

  // Load quiz
  const quiz = await QuizModel.findById(quizId);
  if (!quiz) {
    return res.status(404).json({ message: "Quiz not found" });
  }

  // Load ALL attempts for this quiz, sorted by student then attemptNumber
  const attempts = await model
    .find({ quiz: quizId })
    .populate("user")   // <-- requires your schema to have user: String/ObjectId
    .sort({ user: 1, attemptNumber: 1 });

  return res.json({
    quizId,
    totalAttempts: attempts.length,
    attempts
  });
};


const getAttemptStatus = async (req, res) => {
  const { quizId, attemptId } = req.params;
  const currentUser = req.session?.currentUser;

  if (!currentUser) {
    return res.status(401).json({ message: "Unauthorized" });
  }

  // Load quiz
  const quiz = await QuizModel.findById(quizId);
  if (!quiz) {
    return res.status(404).json({ message: "Quiz not found" });
  }

  // Load attempt
  const attempt = await dao.findAttemptById(attemptId);
  if (!attempt) {
    return res.status(404).json({ message: "Attempt not found" });
  }

  // Students can ONLY see their own attempt status
  if (currentUser.role === "STUDENT" && attempt.user !== currentUser._id) {
    return res.status(403).json({ message: "Forbidden" });
  }

  // If attempt already submitted
  if (attempt.submittedAt) {
    return res.json({
      inProgress: false,
      submitted: true,
      expired: false,
      attemptId: attempt._id
    });
  }

  // No time limit → attempt always in progress until submitted
  if (!quiz.timeLimit || quiz.timeLimit <= 0) {
    return res.json({
      inProgress: true,
      timeRemainingMs: null, // unlimited
      submitted: false,
      expired: false,
      attemptId: attempt._id
    });
  }

  // Time limit enforcement
  const limitMs = quiz.timeLimit * 60 * 1000;
  const deadline = new Date(attempt.startedAt.getTime() + limitMs);
  const now = new Date();

  const expired = now > deadline;
  const timeRemainingMs = Math.max(deadline - now, 0);

  if (expired) {
    return res.json({
      inProgress: false,
      expired: true,
      submitted: false,
      timeRemainingMs: 0,
      deadline,
      attemptId: attempt._id,
      message: "Time limit exceeded"
    });
  }

  // Still active
  res.json({
    inProgress: true,
    expired: false,
    submitted: false,
    startedAt: attempt.startedAt,
    deadline,
    timeRemainingMs,
    attemptId: attempt._id
  });
};




  // ----------------------------------------------------
  // Get attempt by ID (alternative route for convenience)
  // ----------------------------------------------------
  const getAttemptByIdDirect = async (req, res) => {
    const { attemptId } = req.params;
    const currentUser = req.session?.currentUser;

    if (!currentUser) {
      return res.status(401).json({ message: "Unauthorized" });
    }

    const attempt = await dao.findAttemptById(attemptId);
    if (!attempt) {
      return res.status(404).json({ message: "Attempt not found" });
    }

    // Students can only view their own attempts
    if (currentUser.role === "STUDENT" && attempt.user !== currentUser._id) {
      return res.status(403).json({ message: "Forbidden" });
    }

    res.json(attempt);
  };

  // ----------------------------------------------------
  // ROUTES
  // ----------------------------------------------------
  app.post("/api/quizzes/:quizId/start", startAttempt);
  app.get("/api/quizzes/:quizId/attempts", findAttemptsForQuiz);
  app.get("/api/quizzes/:quizId/attempts/latest", findLatestAttempt);
  app.post("/api/quizzes/:quizId/attempts", createAttempt);
  app.post("/api/quizzes/:quizId/preview", previewQuiz);
  app.get("/api/quizzes/:quizId/attempts/:attemptId/next", getNextQuestion);
  app.patch("/api/quizzes/:quizId/attempts/:attemptId/answer", answerOneQuestion);
  app.get("/api/quizzes/:quizId/attempts/:attemptId", getAttemptById);
  app.get("/api/quizzes/:quizId/attempts/all", getAllAttemptsForQuiz);
  app.get("/api/quizzes/:quizId/attempts/:attemptId/status", getAttemptStatus);
  app.get("/api/quiz-attempts/:attemptId", getAttemptByIdDirect);

}
