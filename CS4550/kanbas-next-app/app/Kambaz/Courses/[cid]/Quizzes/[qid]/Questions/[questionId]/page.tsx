"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  findQuestionsForQuiz,
  deleteQuestion
} from "../../../../../client";

export default function ManageQuestionsPage() {
  const { cid, qid } = useParams();
  const router = useRouter();

  const [questions, setQuestions] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    const qs = await findQuestionsForQuiz(qid as string);
    setQuestions(qs);
    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  const handleDelete = async (questionId: string) => {
    if (!confirm("Delete this question?")) return;
    await deleteQuestion(questionId);
    load(); // reload
  };

  if (loading) return <div className="p-6">Loading...</div>;

  return (
    <div className="p-6 flex flex-col gap-6 max-w-3xl">
      <h1 className="text-3xl font-semibold">Manage Questions</h1>

      <button
        onClick={() =>
          router.push(
            `/Kambaz/Courses/${cid}/Quizzes/${qid}/Questions/new`
          )
        }
        className="px-4 py-2 bg-blue-600 text-white rounded"
      >
        Add New Question
      </button>

      <div className="flex flex-col gap-4">
        {questions.length === 0 && (
          <div className="text-gray-600">No questions yet.</div>
        )}

        {questions.map((q) => (
          <div
            key={q._id}
            className="border rounded p-4 flex justify-between items-center bg-gray-50"
          >
            <div>
              <p className="font-semibold">{q.title}</p>
              <p className="text-gray-700 text-sm">{q.points} points</p>
              <p className="text-sm text-gray-600">Type: {q.type}</p>
            </div>

            <div className="flex gap-3">
              <button
                onClick={() =>
                  router.push(
                    `/Kambaz/Courses/${cid}/Quizzes/${qid}/Questions/${q._id}`
                  )
                }
                className="px-3 py-1 bg-green-600 text-white rounded"
              >
                Edit
              </button>

              <button
                onClick={() => handleDelete(q._id)}
                className="px-3 py-1 bg-red-600 text-white rounded"
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>

      <button
        className="mt-6 px-4 py-2 bg-gray-700 text-white rounded"
        onClick={() =>
          router.push(`/Kambaz/Courses/${cid}/Quizzes/${qid}`)
        }
      >
        Back to Quiz
      </button>
    </div>
  );
}
