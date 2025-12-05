import axios from "axios";
const axiosWithCredentials = axios.create({ withCredentials: true });
const HTTP_SERVER = process.env.NEXT_PUBLIC_HTTP_SERVER;
const COURSES_API = `${HTTP_SERVER}/api/courses`;
const USERS_API = `${HTTP_SERVER}/api/users`;
const MODULES_API = `${HTTP_SERVER}/api/modules`;
const ASSIGNMENTS_API = `${HTTP_SERVER}/api/assignments`;
const ENROLLMENTS_API = `${HTTP_SERVER}/api/users`;
const QUIZZES_API = `${HTTP_SERVER}/api/quizzes`;
const QUESTIONS_API = `${HTTP_SERVER}/api/questions`;
const ATTEMPTS_API = `${HTTP_SERVER}/api/quizzes`;

export const getAttemptById = async (attemptId: string) => {
  const { data } = await axiosWithCredentials.get(
    `${HTTP_SERVER}/api/quiz-attempts/${attemptId}`
  );
  return data;
};

export const getQuestionsForAttemptReview = async (quizId: string) => {
  const { data } = await axiosWithCredentials.get(
    `${QUIZZES_API}/${quizId}/questions`
  );
  return data;
};

export const findQuestionsForStudentReview = async (quizId: string) => {
  const { data } = await axiosWithCredentials.get(
    `${QUIZZES_API}/${quizId}/questions`
  );
  return data;
};

export const createQuizForCourse = async (courseId: string, quiz: any) => {
  const { data } = await axiosWithCredentials.post(
    `${COURSES_API}/${courseId}/quizzes`,
    quiz
  );
  return data;
};

export const findQuizzesForCourse = async (courseId: string) => {
  const { data } = await axiosWithCredentials.get(`${COURSES_API}/${courseId}/quizzes`);
  return data;
};

export const createQuiz = async (courseId: string, quiz: any) => {
  const { data } = await axiosWithCredentials.post(
    `${COURSES_API}/${courseId}/quizzes`,
    quiz
  );
  return data;
};

export const findQuizById = async (quizId: string) => {
  const { data } = await axiosWithCredentials.get(`${QUIZZES_API}/${quizId}`);
  return data;
};

export const updateQuiz = async (quizId: string, updates: any) => {
  const { data } = await axiosWithCredentials.put(
    `${QUIZZES_API}/${quizId}`,
    updates
  );
  return data;
};

export const deleteQuiz = async (quizId: string) => {
  const { data } = await axiosWithCredentials.delete(
    `${QUIZZES_API}/${quizId}`
  );
  return data;
};

export const publishQuiz = async (quizId: string) => {
  const { data } = await axiosWithCredentials.put(
    `${QUIZZES_API}/${quizId}/publish`
  );
  return data;
};

export const unpublishQuiz = async (quizId: string) => {
  const { data } = await axiosWithCredentials.put(
    `${QUIZZES_API}/${quizId}/unpublish`
  );
  return data;
};

export const findQuestionsForQuiz = async (quizId: string) => {
  const { data } = await axiosWithCredentials.get(`${QUIZZES_API}/${quizId}/questions`);
  return data;
};

export const createQuestionForQuiz = async (quizId: string, question: any) => {
  const { data } = await axiosWithCredentials.post(
    `${QUIZZES_API}/${quizId}/questions`,
    question
  );
  return data;
};

export const updateQuestion = async (questionId: string, updates: any) => {
  const { data } = await axiosWithCredentials.put(
    `${QUESTIONS_API}/${questionId}`,
    updates
  );
  return data;
};

export const deleteQuestion = async (questionId: string) => {
  const { data } = await axiosWithCredentials.delete(
    `${QUESTIONS_API}/${questionId}`
  );
  return data;
};

export const startAttempt = async (quizId: string, accessCode?: string) => {
  const { data } = await axiosWithCredentials.post(
    `${ATTEMPTS_API}/${quizId}/start`,
    { accessCode }
  );
  return data;
};

export const submitAttempt = async (
  quizId: string,
  body: { attemptId: string; answers: any[]; accessCode?: string }
) => {
  const { data } = await axiosWithCredentials.post(
    `${HTTP_SERVER}/api/quizzes/${quizId}/attempts`,
    body
  );
  return data;
};

export const findAttemptsForQuiz = async (quizId: string) => {
  const { data } = await axiosWithCredentials.get(
    `${ATTEMPTS_API}/${quizId}/attempts`
  );
  return data;
};

export const findLatestAttempt = async (quizId: string) => {
  const { data } = await axiosWithCredentials.get(
    `${ATTEMPTS_API}/${quizId}/attempts/latest`
  );
  return data;
};

export const findAttemptById = async (quizId: string, attemptId: string) => {
  const { data } = await axiosWithCredentials.get(
    `${ATTEMPTS_API}/${quizId}/attempts/${attemptId}`
  );
  return data;
};

export const getNextQuestion = async (quizId: string, attemptId: string) => {
  const { data } = await axiosWithCredentials.get(
    `${ATTEMPTS_API}/${quizId}/attempts/${attemptId}/next`
  );
  return data;
};

export const answerOneQuestion = async (
  quizId: string,
  attemptId: string,
  body: { questionId: string; answer: any }
) => {
  const { data } = await axiosWithCredentials.patch(
    `${HTTP_SERVER}/api/quizzes/${quizId}/attempts/${attemptId}/answer`,
    body
  );
  return data;
};

export const findUsersForCourse = async (courseId: string) => {
 const response = await axios.get(`${COURSES_API}/${courseId}/users`);
 return response.data;
};

export const enrollInCourse = async (userId: string, courseId: string) => {
  const { data } = await axiosWithCredentials.post(
    `${USERS_API}/${userId}/courses/${courseId}`
  );
  return data;
};

export const unenrollFromCourse = async (userId: string, courseId: string) => {
  const { data } = await axiosWithCredentials.delete(
    `${USERS_API}/${userId}/courses/${courseId}`
  );
  return data;
};

export const findEnrollmentsForUser = async (userId: string) => {
  const { data } = await axiosWithCredentials.get(
    `${USERS_API}/${userId}/courses`
  );
  return data;
};



export const findAssignmentsForCourse = async (courseId: string) => {
  const { data } = await axios.get(
    `${COURSES_API}/${courseId}/assignments`
  );
  return data;
};

export const createAssignmentForCourse = async (
  courseId: string,
  assignment: any
) => {
  const { data } = await axios.post(
    `${COURSES_API}/${courseId}/assignments`,
    assignment
  );
  return data;
};

export const updateAssignment = async (assignment: any) => {
  const { data } = await axios.put(
    `${ASSIGNMENTS_API}/${assignment._id}`,
    assignment
  );
  return data;
};

export const deleteAssignment = async (assignmentId: string) => {
  const { data } = await axios.delete(
    `${ASSIGNMENTS_API}/${assignmentId}`
  );
  return data;
};




export const deleteModule = async (courseId: string, moduleId: string) => {
 const response = await axios.delete(
   `${COURSES_API}/${courseId}/modules/${moduleId}`
 );
 return response.data;
};


export const updateModule = async (courseId: string, module: any) => {
 const { data } = await axios.put(
   `${COURSES_API}/${courseId}/modules/${module._id}`,
   module
 );
 return data;
};



export const createModuleForCourse = async (courseId: string, module: any) => {
  const response = await axios.post(
    `${COURSES_API}/${courseId}/modules`,
    module
  );
  return response.data;
};


export const fetchAllCourses = async () => {
  const { data } = await axios.get(COURSES_API);
  return data;
};

export const findMyCourses = async () => {
  const { data } = await axiosWithCredentials.get(`${USERS_API}/current/courses`);
  return data;
};

export const createCourse = async (course: any) => {
  const { data } = await axiosWithCredentials.post(`${USERS_API}/current/courses`, course);
  return data;
};

export const deleteCourse = async (id: string) => {
  const { data } = await axios.delete(`${COURSES_API}/${id}`);
  return data;
};

export const updateCourse = async (course: any) => {
  const { data } = await axios.put(`${COURSES_API}/${course._id}`, course);
  return data;
};

export const findModulesForCourse = async (courseId: string) => {
  const response = await axios
    .get(`${COURSES_API}/${courseId}/modules`);
  return response.data;
};
