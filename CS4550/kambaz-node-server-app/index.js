import "dotenv/config";
import session from "express-session";
import express from "express";
import cors from "cors";

import Hello from "./Hello.js";
import Lab5 from "./Lab5/index.js";

import db from "./Kambaz/Database/index.js";
import UserRoutes from "./Kambaz/Users/routes.js";
import CourseRoutes from "./Kambaz/Courses/routes.js";
import ModulesRoutes from "./Kambaz/Modules/routes.js";
import AssignmentsRoutes from "./Kambaz/Assignments/routes.js";
import EnrollmentsRoutes from "./Kambaz/Enrollments/routes.js";
import QuizzesRoutes from "./Kambaz/Quizzes/routes.js";
import QuestionsRoutes from "./Kambaz/Questions/routes.js";
import QuizAttemptsRoutes from "./Kambaz/QuizAttempts/routes.js";
import mongoose from "mongoose";

const CONNECTION_STRING =
  process.env.DATABASE_CONNECTION_STRING ||
  "mongodb://127.0.0.1:27017/kambaz";
mongoose.connect(CONNECTION_STRING);

mongoose.connection.on("connected", () => {
  console.log("MongoDB connected:", CONNECTION_STRING);
});

mongoose.connection.on("error", (err) => {
  console.error("MongoDB connection error:", err);
});

const app = express();

app.use(
  cors({
    credentials: true,
    origin: process.env.CLIENT_URL || "http://localhost:3000",
  })
);

app.use(express.json());

const sessionOptions = {
  secret: process.env.SESSION_SECRET || "kambaz",
  resave: false,
  saveUninitialized: false,
  cookie: {
    httpOnly: true,
    sameSite: "lax",
    secure: false,
  },
};

if (process.env.SERVER_ENV === "production") {
  sessionOptions.proxy = true;
  sessionOptions.cookie.sameSite = "none";
  sessionOptions.cookie.secure = true;
}

app.use(session(sessionOptions));

Hello(app);
Lab5(app);
UserRoutes(app, db);
CourseRoutes(app, db);
ModulesRoutes(app, db);
AssignmentsRoutes(app, db);
EnrollmentsRoutes(app, db);
QuizzesRoutes(app);
QuestionsRoutes(app);
QuizAttemptsRoutes(app);

const PORT = process.env.PORT || 4000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
