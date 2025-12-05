import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  assignments: [],
  assignment: {},
};

const assignmentsSlice = createSlice({
  name: "assignments",
  initialState,
  reducers: {
    setAssignments: (state, action) => {
      state.assignments = action.payload;
    },

    addAssignment: (state, action) => {
      state.assignments.push(action.payload);
    },

    deleteAssignment: (state, action) => {
      const assignmentId = action.payload;
      state.assignments = state.assignments.filter(
        (a: any) => a._id !== assignmentId
      );
    },

    updateAssignment: (state, action) => {
      const updated = action.payload;
      state.assignments = state.assignments.map((a: any) =>
        a._id === updated._id ? updated : a
      );
    },

    setAssignment: (state, action) => {
      state.assignment = action.payload;
    },
  },
});

export const {
  setAssignments,
  addAssignment,
  deleteAssignment,
  updateAssignment,
  setAssignment,
} = assignmentsSlice.actions;

export default assignmentsSlice.reducer;
