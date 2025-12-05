import React from "react";
import { Button, ListGroupItem } from "react-bootstrap";
import { useDispatch } from "react-redux";
import { deleteTodo, setTodo } from "./todosReducer";

export default function TodoItem({ todo }: { todo: { id: string; title: string } }) {
  const dispatch = useDispatch();

  return (
    <ListGroupItem>
      <Button
        onClick={() => dispatch(deleteTodo(todo.id))}
        id="wd-delete-todo-click"
        variant="danger"
        className="me-2"
      >
        Delete
      </Button>
      <Button
        onClick={() => dispatch(setTodo(todo))}
        id="wd-set-todo-click"
        variant="warning"
        className="me-2"
      >
        Edit
      </Button>
      {todo.title}
    </ListGroupItem>
  );
}
