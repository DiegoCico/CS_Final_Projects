import mongoose from "mongoose";

const quizAttemptSchema = new mongoose.Schema(
  {
    _id: String,
    quiz: String,
    user: String,
    attemptNumber: { type: Number, default: 1 },
    answers: [{
      question: String,
      answer: mongoose.Schema.Types.Mixed,
      isCorrect: Boolean,
      pointsEarned: Number
    }],
    score: { type: Number, default: 0 },
    totalPoints: { type: Number, default: 0 },
    startedAt: { type: Date, default: Date.now },
    submittedAt: Date
  },
  { collection: "quiz_attempts" }
);

export default quizAttemptSchema;
