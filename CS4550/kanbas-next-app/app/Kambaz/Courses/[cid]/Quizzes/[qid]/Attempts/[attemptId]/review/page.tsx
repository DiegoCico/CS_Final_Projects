"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import {
  getAttemptById,
  getQuestionsForAttemptReview,
  findQuizById,
} from "../../../../../../client";
import { useSelector } from "react-redux";

export default function ReviewAttemptPage() {
  const { cid, qid, attemptId } = useParams();
  const currentUser = useSelector((state: any) => state.account.currentUser);

  const [attempt, setAttempt] = useState<any>(null);
  const [questions, setQuestions] = useState<any[]>([]);
  const [quiz, setQuiz] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    const q = await findQuizById(qid as string);
    setQuiz(q);

    const att = await getAttemptById(attemptId as string);
    setAttempt(att);

    // Student → backend already sanitizes questions
    // Faculty → sees full questions
    const qs = await getQuestionsForAttemptReview(qid as string);
    setQuestions(qs);

    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  if (loading) return <div className="p-6">Loading...</div>;
  if (!attempt || !quiz)
    return <div className="p-6">Attempt not found</div>;

  return (
    <div className="p-6 max-w-4xl flex flex-col gap-6">

      <h1 className="text-3xl font-semibold">Attempt Review</h1>

      <div className="border rounded p-4 bg-gray-50">
        <p><strong>Score:</strong> {attempt.score} / {attempt.totalPoints}</p>
        <p><strong>Submitted:</strong> {new Date(attempt.submittedAt).toLocaleString()}</p>
      </div>

      <div className="flex flex-col gap-6">
        {questions.map((q, i) => {
          const studentAns = attempt.answers.find((a: any) => a.question === q._id);

          return (
            <div key={q._id} className="border rounded p-4">
              <h2 className="text-xl font-semibold mb-2">
                Question {i + 1}
              </h2>

              <p className="font-medium mb-2">{q.title}</p>

              {/* ---------------------- */}
              {/* STUDENT ANSWER */}
              {/* ---------------------- */}
              <p>
                <strong>Your Answer:</strong>{" "}
                {renderAnswer(q, studentAns?.answer)}
              </p>

              {/* ---------------------- */}
              {/* CORRECT ANSWER? depends on rules */}
              {/* ---------------------- */}
              {renderCorrectnessBlock(q, quiz, currentUser, studentAns)}
            </div>
          );
        })}
      </div>
    </div>
  );
}

/* --------------------- */
/*   UI Helper Logic     */
/* --------------------- */

function renderAnswer(q: any, answer: any) {
  if (q.type === "MULTIPLE_CHOICE") {
    const choice = q.choices?.find((c: any) => c._id === answer);
    return choice ? choice.text : "No answer";
  }

  if (q.type === "TRUE_FALSE") {
    return answer ? "True" : "False";
  }

  return answer || "No answer";
}

function renderCorrectnessBlock(q: any, quiz: any, user: any, studentAns: any) {
  // Faculty always sees correct answers
  if (user.role === "FACULTY") {
    return (
      <div className="mt-2 text-green-700">
        <strong>Correct Answer:</strong> {renderCorrectAnswer(q)}
      </div>
    );
  }

  const now = new Date();

  switch (quiz.showCorrectAnswers) {
    case "IMMEDIATELY":
      return (
        <div className="mt-2 text-green-700">
          <strong>Correct Answer:</strong> {renderCorrectAnswer(q)}
        </div>
      );

    case "AFTER_DUE_DATE":
      if (quiz.dueDate && now > new Date(quiz.dueDate)) {
        return (
          <div className="mt-2 text-green-700">
            <strong>Correct Answer:</strong> {renderCorrectAnswer(q)}
          </div>
        );
      }
      return (
        <div className="mt-2 italic text-gray-500">
          Correct answers will be available after the due date.
        </div>
      );

    case "NEVER":
    default:
      return (
        <div className="mt-2 italic text-gray-500">
          Correct answers are not shown for this quiz.
        </div>
      );
  }
}

function renderCorrectAnswer(q: any) {
  if (q.type === "MULTIPLE_CHOICE") {
    const correct = q.choices.find((c: any) => c.isCorrect);
    return correct ? correct.text : "N/A";
  }

  if (q.type === "TRUE_FALSE") {
    return q.correctAnswer ? "True" : "False";
  }

  return q.possibleAnswers?.join(", ") || "N/A";
}
