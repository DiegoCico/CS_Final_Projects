"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  getNextQuestion,
  answerOneQuestion,
  submitAttempt
} from "../../../../../client";

export default function TakeQuizPage() {
  const { cid, qid, attemptId } = useParams();
  const router = useRouter();

  const [loading, setLoading] = useState(true);
  const [done, setDone] = useState(false);
  const [index, setIndex] = useState(0);
  const [question, setQuestion] = useState<any>(null);
  const [answers, setAnswers] = useState<any[]>([]);

  // Load first question
  const loadQuestion = async () => {
    const data = await getNextQuestion(qid as string, attemptId as string);

    if (data.done) {
      setDone(true);
      return;
    }

    setIndex(data.index);
    setQuestion(data.question);
    setLoading(false);
  };

  useEffect(() => {
    loadQuestion();
  }, []);

  // Answer handler
  const handleAnswer = async (value: any) => {
    const body = {
      questionId: question._id,
      answer: value,
    };

    const data = await answerOneQuestion(qid as string, attemptId as string, body);

    // Store student's answer locally for final submission
    setAnswers((prev: any[]) => [...prev, body]);

    if (data.done) {
      setDone(true);
      return;
    }

    setIndex(data.index);
    setQuestion(data.question);
  };

  // Final Submit
  const handleSubmit = async () => {
    const result = await submitAttempt(qid as string, {
      attemptId: attemptId as string,
      answers,
    });

    router.push(
      `/Kambaz/Courses/${cid}/Quizzes/${qid}/Attempts/${attemptId}/review`
    );
  };

  if (loading) return <div className="p-6">Loading question...</div>;

  if (done)
    return (
      <div className="p-6">
        <h1 className="text-xl font-semibold mb-4">Quiz Complete</h1>
        <button
          onClick={handleSubmit}
          className="px-4 py-2 bg-blue-600 text-white rounded"
        >
          Submit Attempt
        </button>
      </div>
    );

  return (
    <div className="p-6 flex flex-col gap-4">
      <h1 className="text-2xl font-semibold">
        Question {index + 1}
      </h1>

      {/* Renders different types */}
      {question.type === "MULTIPLE_CHOICE" && (
        <div className="flex flex-col gap-2">
          <p className="font-semibold">{question.title}</p>
          {question.choices.map((c: any) => (
            <button
              key={c._id}
              className="p-3 border rounded hover:bg-gray-100"
              onClick={() => handleAnswer(c._id)}
            >
              {c.text}
            </button>
          ))}
        </div>
      )}

      {question.type === "TRUE_FALSE" && (
        <div className="flex flex-col gap-2">
          <p className="font-semibold">{question.title}</p>
          <button
            onClick={() => handleAnswer(true)}
            className="p-3 border rounded hover:bg-gray-100"
          >
            True
          </button>
          <button
            onClick={() => handleAnswer(false)}
            className="p-3 border rounded hover:bg-gray-100"
          >
            False
          </button>
        </div>
      )}

      {question.type === "FILL_IN_BLANK" && (
        <div className="flex flex-col gap-2">
          <p className="font-semibold">{question.title}</p>
          <input
            className="border p-2 rounded"
            onBlur={(e) => handleAnswer(e.target.value)}
          />
        </div>
      )}
    </div>
  );
}
