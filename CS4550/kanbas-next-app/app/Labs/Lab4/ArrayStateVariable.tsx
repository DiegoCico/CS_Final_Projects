/* eslint-disable */
import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { ListGroup, ListGroupItem, Button } from "react-bootstrap";
import { addTodo, deleteTodo } from "./ReduxExamples/todos/todosReducer";

export default function ArrayStateVariable() {
  const { todos } = useSelector((state: any) => state.todosReducer);
  const dispatch = useDispatch();

  return (
    <div id="wd-array-state-variables">
      <h2>Array State Variable</h2>

      <Button
        onClick={() =>
          dispatch(addTodo({ id: Date.now().toString(), title: "Random " + Math.floor(Math.random() * 100) }))
        }
        className="me-2"
      >
        Add Element
      </Button>

      <ListGroup>
        {todos.map((todo: any) => (
          <ListGroupItem key={todo.id}>
            {todo.title}
            <Button
              onClick={() => dispatch(deleteTodo(todo.id))}
              variant="danger"
              size="sm"
              className="ms-2"
            >
              Delete
            </Button>
          </ListGroupItem>
        ))}
      </ListGroup>

      <hr />
    </div>
  );
}
