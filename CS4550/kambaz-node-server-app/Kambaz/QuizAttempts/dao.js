import model from "./model.js";
import { v4 as uuidv4 } from "uuid";

export default function QuizAttemptsDao() {

  // Find all attempts for a given quiz & user
  const findAttemptsForQuiz = (quizId, userId) =>
    model.find({ quiz: quizId, user: userId }).sort({ attemptNumber: -1 });

  // Find the most recent attempt
  const findLatestAttempt = async (quizId, userId) => {
    const attempts = await model
      .find({ quiz: quizId, user: userId })
      .sort({ attemptNumber: -1 })
      .limit(1);

    return attempts.length > 0 ? attempts[0] : null;
  };

  // Create a new attempt with UUID
  const createAttempt = (attempt) => {
    const newAttempt = { ...attempt, _id: uuidv4() };
    return model.create(newAttempt);
  };

  // Count attempts for limits
  const countAttempts = (quizId, userId) =>
    model.countDocuments({ quiz: quizId, user: userId });

  // Delete attempts when quiz is deleted
  const deleteAttemptsForQuiz = (quizId) =>
    model.deleteMany({ quiz: quizId });

  // Fetch a single attempt by ID (UUID-safe)
  const findAttemptById = (attemptId) =>
    model.findOne({ _id: attemptId });

  return {
    findAttemptsForQuiz,
    findLatestAttempt,
    createAttempt,
    countAttempts,
    deleteAttemptsForQuiz,
    findAttemptById
  };
}
