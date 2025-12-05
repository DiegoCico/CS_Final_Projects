"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { useSelector } from "react-redux";

export default function AllAttemptsPage() {
  const { cid, qid } = useParams();
  const router = useRouter();
  const currentUser = useSelector((state: any) => state.account.currentUser);

  const [attempts, setAttempts] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_HTTP_SERVER}/api/quizzes/${qid}/attempts/all`,
          { credentials: "include" }
        );
        const data = await response.json();
        setAttempts(data.attempts || []);
      } catch (err) {
        console.error("Failed to load attempts", err);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [qid]);

  if (!currentUser || currentUser.role !== "FACULTY") {
    return <div className="p-6 text-red-600">Unauthorized - Faculty only</div>;
  }

  if (loading) return <div className="p-6">Loading...</div>;

  return (
    <div className="p-6 flex flex-col gap-6 max-w-5xl">
      <h1 className="text-3xl font-semibold">All Quiz Attempts</h1>

      {attempts.length === 0 && (
        <div className="text-gray-600">No attempts yet.</div>
      )}

      <div className="flex flex-col gap-4">
        {attempts.map((att) => (
          <div
            key={att._id}
            className="border rounded p-4 bg-gray-50 flex justify-between items-center"
          >
            <div>
              <p>
                <strong>Student:</strong> {att.user?.firstName} {att.user?.lastName}
              </p>
              <p>
                <strong>Attempt:</strong> {att.attemptNumber}
              </p>
              <p>
                <strong>Score:</strong> {att.score} / {att.totalPoints}
              </p>
              <p className="text-sm text-gray-600">
                Submitted: {new Date(att.submittedAt).toLocaleString()}
              </p>
            </div>

            <button
              onClick={() =>
                router.push(
                  `/Kambaz/Courses/${cid}/Quizzes/${qid}/Attempts/${att._id}/review`
                )
              }
              className="px-4 py-2 bg-blue-600 text-white rounded"
            >
              View
            </button>
          </div>
        ))}
      </div>

      <button
        className="mt-4 px-4 py-2 bg-gray-700 text-white rounded"
        onClick={() => router.push(`/Kambaz/Courses/${cid}/Quizzes/${qid}`)}
      >
        Back to Quiz
      </button>
    </div>
  );
}
