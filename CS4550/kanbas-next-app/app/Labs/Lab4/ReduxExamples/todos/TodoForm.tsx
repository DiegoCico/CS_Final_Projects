/* eslint-disable */
import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { Button, FormControl, ListGroupItem } from "react-bootstrap";
import { addTodo, updateTodo, setTodo } from "./todosReducer";

export default function TodoForm() {
  const { todo } = useSelector((state: any) => state.todosReducer);
  const dispatch = useDispatch();

  return (
    <ListGroupItem>
      <Button
        onClick={() => dispatch(addTodo(todo))}
        id="wd-add-todo-click"
        className="me-2"
      >
        Add
      </Button>
      <Button
        onClick={() => dispatch(updateTodo(todo))}
        id="wd-update-todo-click"
        className="me-2"
      >
        Update
      </Button>
      <FormControl
        value={todo.title}
        onChange={(e) =>
          dispatch(setTodo({ ...todo, title: e.target.value }))
        }
        placeholder="Enter todo..."
      />
    </ListGroupItem>
  );
}
