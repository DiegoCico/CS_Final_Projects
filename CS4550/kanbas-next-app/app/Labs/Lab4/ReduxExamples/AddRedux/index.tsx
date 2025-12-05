/* eslint-disable */
"use client";

export const dynamic = "force-dynamic"; 

import { useSelector, useDispatch } from "react-redux";
import { useState } from "react";
import { add } from "./addReducer";
import { Button, FormControl } from "react-bootstrap";

export default function AddRedux() {
  const dispatch = useDispatch();

  // let sum = 0;
  // try {
  const state = useSelector((state) => state);
  console.log(state)
    // sum = state?.sum ?? 0;
  // } catch {
  // }

  const [a, setA] = useState(12);
  const [b, setB] = useState(23);

  return (
    <div className="w-25" id="wd-add-redux">
      <h1>Add Redux</h1>
      <h2>
        {a} + {b} = 123
      </h2>

      <FormControl
        type="number"
        defaultValue={a}
        onChange={(e) => setA(parseInt(e.target.value))}
      />
      <FormControl
        type="number"
        defaultValue={b}
        onChange={(e) => setB(parseInt(e.target.value))}
      />

      <Button
        id="wd-add-redux-click"
        onClick={() => dispatch(add({ a, b }))}
      >
        Add Redux
      </Button>

      <hr />
    </div>
  );
}
