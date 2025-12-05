"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  findQuestionById,
  updateQuestion,
} from "../../../../../client";

export default function EditQuestionPage() {
  const { cid, qid, questionId } = useParams();
  const router = useRouter();

  const [question, setQuestion] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  const load = async () => {
    const q = await findQuestionById(questionId as string);
    setQuestion(q);
    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  // -------------------------
  // Handlers
  // -------------------------
  const handleSave = async () => {
    await updateQuestion(questionId as string, question);
    router.push(`/Kambaz/Courses/${cid}/Quizzes/${qid}/Questions`);
  };

  const updateField = (field: string, value: any) => {
    setQuestion({ ...question, [field]: value });
  };

  const updateChoice = (index: number, field: string, value: any) => {
    const updated = [...question.choices];
    updated[index] = { ...updated[index], [field]: value };
    setQuestion({ ...question, choices: updated });
  };

  const addChoice = () => {
    setQuestion({
      ...question,
      choices: [...question.choices, { _id: crypto.randomUUID(), text: "", isCorrect: false }],
    });
  };

  const deleteChoice = (index: number) => {
    const updated = [...question.choices];
    updated.splice(index, 1);
    setQuestion({ ...question, choices: updated });
  };

  if (loading) return <div className="p-6">Loading...</div>;
  if (!question) return <div className="p-6">Question not found</div>;

  return (
    <div className="p-6 flex flex-col gap-6 max-w-2xl">

      <h1 className="text-2xl font-semibold">Edit Question</h1>

      {/* Title */}
      <div>
        <label className="font-semibold block mb-1">Title</label>
        <input
          value={question.title}
          onChange={(e) => updateField("title", e.target.value)}
          className="border p-2 w-full rounded"
        />
      </div>

      {/* Points */}
      <div>
        <label className="font-semibold block mb-1">Points</label>
        <input
          type="number"
          value={question.points}
          onChange={(e) => updateField("points", Number(e.target.value))}
          className="border p-2 w-full rounded"
        />
      </div>

      {/* Type */}
      <div>
        <label className="font-semibold block mb-1">Question Type</label>
        <select
          value={question.type}
          onChange={(e) => updateField("type", e.target.value)}
          className="border p-2 w-full rounded"
        >
          <option value="MULTIPLE_CHOICE">Multiple Choice</option>
          <option value="TRUE_FALSE">True/False</option>
          <option value="FILL_IN_BLANK">Fill in Blank</option>
        </select>
      </div>

      {/* ------------------------- */}
      {/* MULTIPLE CHOICE UI */}
      {/* ------------------------- */}
      {question.type === "MULTIPLE_CHOICE" && (
        <div className="flex flex-col gap-4">
          <h2 className="font-semibold">Choices</h2>

          {question.choices.map((c: any, index: number) => (
            <div key={c._id} className="border p-3 rounded bg-gray-50">
              <input
                value={c.text}
                onChange={(e) => updateChoice(index, "text", e.target.value)}
                className="border p-2 w-full rounded mb-2"
              />

              <label className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={c.isCorrect}
                  onChange={(e) =>
                    updateChoice(index, "isCorrect", e.target.checked)
                  }
                />
                Correct Answer
              </label>

              <button
                onClick={() => deleteChoice(index)}
                className="mt-2 px-3 py-1 bg-red-600 text-white rounded"
              >
                Delete Choice
              </button>
            </div>
          ))}

          <button
            onClick={addChoice}
            className="px-4 py-2 bg-blue-600 text-white rounded"
          >
            Add Choice
          </button>
        </div>
      )}

      {/* ------------------------- */}
      {/* TRUE/FALSE UI */}
      {/* ------------------------- */}
      {question.type === "TRUE_FALSE" && (
        <div>
          <label className="font-semibold block mb-2">Correct Answer</label>
          <select
            value={question.correctAnswer ? "true" : "false"}
            onChange={(e) =>
              updateField("correctAnswer", e.target.value === "true")
            }
            className="border p-2 w-full rounded"
          >
            <option value="true">True</option>
            <option value="false">False</option>
          </select>
        </div>
      )}

      {/* ------------------------- */}
      {/* FILL IN BLANK UI */}
      {/* ------------------------- */}
      {question.type === "FILL_IN_BLANK" && (
        <div>
          <label className="font-semibold block mb-1">
            Acceptable Answers (comma separated)
          </label>
          <input
            value={question.possibleAnswers?.join(", ") || ""}
            onChange={(e) =>
              updateField(
                "possibleAnswers",
                e.target.value.split(",").map((a) => a.trim())
              )
            }
            className="border p-2 w-full rounded mb-3"
          />

          <label className="flex items-center gap-2">
            <input
              type="checkbox"
              checked={question.caseSensitive}
              onChange={(e) => updateField("caseSensitive", e.target.checked)}
            />
            Case Sensitive
          </label>
        </div>
      )}

      {/* ------------------------- */}
      {/* ACTION BUTTONS */}
      {/* ------------------------- */}
      <button
        onClick={handleSave}
        className="bg-green-600 text-white px-4 py-2 rounded"
      >
        Save Question
      </button>

      <button
        onClick={() =>
          router.push(`/Kambaz/Courses/${cid}/Quizzes/${qid}/Questions`)
        }
        className="bg-gray-700 text-white px-4 py-2 rounded"
      >
        Back
      </button>
    </div>
  );
}
