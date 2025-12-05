/* eslint-disable */
"use client";

import { useSelector, useDispatch } from "react-redux";
import { increment, decrement } from "./counterReducer";

export default function CounterRedux() {
  const dispatch = useDispatch();

  let count = 0;
  try {
    const state = useSelector((state: any) => state?.counterReducer);
    count = state?.count ?? 0;
  } catch {
  }

  return (
    <div id="wd-counter-redux">
      <h2>Counter Redux</h2>
      <h3>{count}</h3>
      <button
        onClick={() => dispatch(increment())}
        id="wd-counter-redux-increment-click"
      >
        Increment
      </button>
      <button
        onClick={() => dispatch(decrement())}
        id="wd-counter-redux-decrement-click"
      >
        Decrement
      </button>
      <hr />
    </div>
  );
}
