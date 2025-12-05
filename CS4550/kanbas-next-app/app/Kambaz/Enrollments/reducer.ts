import { createSlice } from "@reduxjs/toolkit";

interface Enrollment {
  _id: string;
  user: string;
  course: string;
}

interface EnrollmentState {
  enrollments: Enrollment[];
  showAllCourses: boolean;
}

const initialState: EnrollmentState = {
  enrollments: [],
  showAllCourses: false,
};

const enrollmentsSlice = createSlice({
  name: "enrollments",
  initialState,
  reducers: {
    toggleShowAllCourses: (state) => {
      state.showAllCourses = !state.showAllCourses;
    },

    setEnrollments: (state, action) => {
      state.enrollments = action.payload;
    },

    addEnrollment: (state, action) => {
      state.enrollments.push(action.payload);
    },

    removeEnrollment: (state, action) => {
      const { user, course } = action.payload;
      state.enrollments = state.enrollments.filter(
        (e) => !(e.user === user && e.course === course)
      );
    },
  },
});

export const {
  toggleShowAllCourses,
  setEnrollments,
  addEnrollment,
  removeEnrollment,
} = enrollmentsSlice.actions;

export default enrollmentsSlice.reducer;
