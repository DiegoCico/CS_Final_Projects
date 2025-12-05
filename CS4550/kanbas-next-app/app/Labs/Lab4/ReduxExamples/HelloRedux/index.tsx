/* eslint-disable */
"use client";

import { useSelector } from "react-redux";

export default function HelloRedux() {
  let message = "Hello World"; 

  try {
    const state = useSelector((state: any) => state?.helloReducer);
    message = state?.message ?? message;
  } catch {
  }

  return (
    <div id="wd-hello-redux">
      <h3>Hello Redux</h3>
      <h4>{message}</h4>
      <hr />
    </div>
  );
}
