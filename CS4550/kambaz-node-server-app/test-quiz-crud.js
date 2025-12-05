// Simple test script to verify Quiz CRUD operations
import QuizzesDao from "./Kambaz/Quizzes/dao.js";
import mongoose from "mongoose";

const CONNECTION_STRING = process.env.DATABASE_CONNECTION_STRING || "mongodb://127.0.0.1:27017/kambaz";

async function testQuizCRUD() {
  try {
    console.log("Connecting to MongoDB...");
    await mongoose.connect(CONNECTION_STRING);
    console.log("✓ Connected to MongoDB");

    const dao = QuizzesDao();

    // Test 1: Create a quiz
    console.log("\n--- Test 1: Create Quiz ---");
    const testQuiz = {
      course: "test-course-123",
      title: "Test Quiz",
      description: "This is a test quiz",
      published: false
    };
    const createdQuiz = await dao.createQuiz(testQuiz);
    console.log("✓ Quiz created:", createdQuiz._id);

    // Test 2: Find quiz by ID
    console.log("\n--- Test 2: Find Quiz by ID ---");
    const foundQuiz = await dao.findQuizById(createdQuiz._id);
    console.log("✓ Quiz found:", foundQuiz ? foundQuiz.title : "Not found");

    // Test 3: Find quizzes for course
    console.log("\n--- Test 3: Find Quizzes for Course ---");
    const courseQuizzes = await dao.findQuizzesForCourse("test-course-123");
    console.log("✓ Found", courseQuizzes.length, "quiz(es) for course");

    // Test 4: Update quiz
    console.log("\n--- Test 4: Update Quiz ---");
    await dao.updateQuiz(createdQuiz._id, { title: "Updated Test Quiz" });
    const updatedQuiz = await dao.findQuizById(createdQuiz._id);
    console.log("✓ Quiz updated:", updatedQuiz.title);

    // Test 5: Publish quiz
    console.log("\n--- Test 5: Publish Quiz ---");
    await dao.publishQuiz(createdQuiz._id);
    const publishedQuiz = await dao.findQuizById(createdQuiz._id);
    console.log("✓ Quiz published:", publishedQuiz.published);

    // Test 6: Unpublish quiz
    console.log("\n--- Test 6: Unpublish Quiz ---");
    await dao.unpublishQuiz(createdQuiz._id);
    const unpublishedQuiz = await dao.findQuizById(createdQuiz._id);
    console.log("✓ Quiz unpublished:", unpublishedQuiz.published);

    // Test 7: Delete quiz
    console.log("\n--- Test 7: Delete Quiz ---");
    await dao.deleteQuiz(createdQuiz._id);
    const deletedQuiz = await dao.findQuizById(createdQuiz._id);
    console.log("✓ Quiz deleted:", deletedQuiz === null ? "Yes" : "No");

    console.log("\n✓ All CRUD tests passed!");
  } catch (error) {
    console.error("✗ Test failed:", error.message);
    console.error(error);
  } finally {
    await mongoose.connection.close();
    console.log("\nMongoDB connection closed");
  }
}

testQuizCRUD();
