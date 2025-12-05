"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  findQuizById,
  startAttempt,
  findLatestAttempt,
  findAttemptsForQuiz,
} from "../../../client";
import { useSelector } from "react-redux";

export default function QuizDetailPage() {
  const { cid, qid } = useParams();
  const router = useRouter();

  const currentUser = useSelector((state: any) => state.account.currentUser);

  const [quiz, setQuiz] = useState<any>(null);
  const [attempts, setAttempts] = useState<any[]>([]);
  const [latestAttempt, setLatestAttempt] = useState<any>(null);

  const [accessCode, setAccessCode] = useState("");
  const [loading, setLoading] = useState(true);

  const load = async () => {
    const q = await findQuizById(qid as string);
    setQuiz(q);

    if (currentUser?.role === "STUDENT") {
      const a = await findAttemptsForQuiz(qid as string);
      setAttempts(a);

      const la = await findLatestAttempt(qid as string);
      setLatestAttempt(la || null);
    }

    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  if (loading) return <div className="p-6">Loading...</div>;
  if (!quiz) return <div className="p-6">Quiz not found</div>;

  const now = new Date();
  const available = !quiz.availableDate || now >= new Date(quiz.availableDate);
  const open = !quiz.untilDate || now <= new Date(quiz.untilDate);

  // ------------------------------
  // Attempt limit logic
  // ------------------------------
  const attemptsUsed = attempts.length;
  const attemptsAllowed = quiz.multipleAttempts ? quiz.howManyAttempts : 1;
  const attemptsRemaining = attemptsAllowed - attemptsUsed;

  const exhausted = attemptsRemaining <= 0;

  const handleStart = async () => {
    try {
      const attempt = await startAttempt(qid as string, accessCode);
      router.push(
        `/Kambaz/Courses/${cid}/Quizzes/${qid}/Take/${attempt._id}`
      );
    } catch (err: any) {
      alert(err?.response?.data?.message || "Unable to start quiz");
    }
  };

  return (
    <div className="p-6 flex flex-col gap-6 max-w-3xl">

      <h1 className="text-3xl font-semibold">{quiz.title}</h1>

      {quiz.description && (
        <p className="text-gray-700">{quiz.description}</p>
      )}

      {/* QUIZ METADATA */}
      <div className="border p-4 rounded bg-gray-50 space-y-1">
        <p><strong>Points:</strong> {quiz.points}</p>
        <p><strong>Time Limit:</strong> {quiz.timeLimit || 0} minutes</p>
        <p><strong>Attempts:</strong> {attemptsUsed} / {attemptsAllowed}</p>
        {quiz.accessCode && <p><strong>Access Code Required</strong></p>}
        {quiz.availableDate && (
          <p><strong>Opens:</strong> {new Date(quiz.availableDate).toLocaleString()}</p>
        )}
        {quiz.untilDate && (
          <p><strong>Closes:</strong> {new Date(quiz.untilDate).toLocaleString()}</p>
        )}
      </div>

      {/* STUDENT VIEW */}
      {currentUser?.role === "STUDENT" && (
        <div className="flex flex-col gap-4">

          {!quiz.published && (
            <p className="text-red-600 font-semibold">
              This quiz is not published.
            </p>
          )}

          {!available && (
            <p className="text-red-600 font-semibold">
              Quiz opens on {quiz.availableDate}
            </p>
          )}

          {!open && (
            <p className="text-red-600 font-semibold">
              Quiz closed on {quiz.untilDate}
            </p>
          )}

          {exhausted && (
            <p className="text-red-600 font-semibold">
              You have no attempts remaining.
            </p>
          )}

          {quiz.accessCode && (
            <input
              type="password"
              placeholder="Enter Access Code"
              value={accessCode}
              onChange={(e) => setAccessCode(e.target.value)}
              className="border p-2 rounded w-full"
            />
          )}

          <button
            onClick={handleStart}
            disabled={
              !quiz.published || !available || !open || exhausted
            }
            className={`px-4 py-2 rounded text-white ${
              !quiz.published || !available || !open || exhausted
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            Start Quiz
          </button>

          {/* Latest Attempt */}
          {latestAttempt && (
            <div className="border p-4 rounded mt-4">
              <h2 className="text-xl font-semibold">Your Latest Attempt</h2>
              <p>Score: {latestAttempt.score}/{latestAttempt.totalPoints}</p>

              <button
                className="mt-2 px-3 py-1 bg-gray-800 text-white rounded"
                onClick={() =>
                  router.push(
                    `/Kambaz/Courses/${cid}/Quizzes/${qid}/Attempts/${latestAttempt._id}/review`
                  )
                }
              >
                Review Attempt
              </button>
            </div>
          )}
        </div>
      )}

      {/* FACULTY VIEW */}
      {currentUser?.role === "FACULTY" && (
        <div className="flex flex-col gap-3">
          <button
            onClick={() =>
              router.push(`/Kambaz/Courses/${cid}/Quizzes/${qid}/edit`)
            }
            className="bg-green-600 text-white px-4 py-2 rounded"
          >
            Edit Quiz
          </button>

          <button
            onClick={() =>
              router.push(`/Kambaz/Courses/${cid}/Quizzes/${qid}/Questions`)
            }
            className="bg-purple-600 text-white px-4 py-2 rounded"
          >
            Manage Questions
          </button>

          <button
            onClick={() =>
              router.push(`/Kambaz/Courses/${cid}/Quizzes/${qid}/Attempts`)
            }
            className="bg-orange-600 text-white px-4 py-2 rounded"
          >
            View Attempts
          </button>
        </div>
      )}
    </div>
  );
}
