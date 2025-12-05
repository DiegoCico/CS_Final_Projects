import model from "./model.js";
import { v4 as uuidv4 } from "uuid";

export default function QuestionsDao() {
  const findQuestionsForQuiz = (quizId) =>
    model.find({ quiz: quizId }).sort({ order: 1 });

  const findQuestionById = (questionId) =>
    model.findById(questionId);

  const createQuestion = (question) => {
    const newQuestion = { ...question, _id: uuidv4() };
    return model.create(newQuestion);
  };

  const updateQuestion = (questionId, updates) =>
    model.updateOne({ _id: questionId }, { $set: updates });

  const deleteQuestion = (questionId) =>
    model.deleteOne({ _id: questionId });

  const deleteQuestionsForQuiz = (quizId) =>
    model.deleteMany({ quiz: quizId });

  return {
    findQuestionsForQuiz,
    findQuestionById,
    createQuestion,
    updateQuestion,
    deleteQuestion,
    deleteQuestionsForQuiz,
  };
}
