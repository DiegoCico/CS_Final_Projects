import mongoose from "mongoose";

const questionSchema = new mongoose.Schema(
  {
    _id: String,
    quiz: String,
    type: {
      type: String,
      enum: ["MULTIPLE_CHOICE", "TRUE_FALSE", "FILL_IN_BLANK"],
      default: "MULTIPLE_CHOICE"
    },
    title: String,
    points: { type: Number, default: 0 },
    question: String,
    
    // For multiple choice
    choices: [{
      _id: String,
      text: String,
      isCorrect: Boolean
    }],
    
    // For true/false
    correctAnswer: Boolean,
    
    // For fill in blank
    possibleAnswers: [String],
    caseSensitive: { type: Boolean, default: false },
    
    order: { type: Number, default: 0 }
  },
  { collection: "questions" }
);

export default questionSchema;
