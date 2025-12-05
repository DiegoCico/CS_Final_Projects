"use client";

import Link from "next/link";
import { ListGroup, ListGroupItem } from "react-bootstrap";
import { usePathname } from "next/navigation";

export default function CourseNavigation({ cid }: { cid: string }) {
  const pathname = usePathname();

  const links = [
    { label: "Home", path: "Home" },
    { label: "Modules", path: "Modules" },
    { label: "Piazza", path: "Piazza" },
    { label: "Zoom", path: "Zoom" },
    { label: "Assignments", path: "Assignments" },
    { label: "Quizzes", path: "Quizzes" },
    { label: "Grades", path: "Grades" },
    { label: "People", path: "People" },
  ];

  return (
    <ListGroup id="wd-course-navigation" className="rounded-0">
      {links.map(({ label, path }) => {
        const href = `/Kambaz/Courses/${cid}/${path}`;
        const active = pathname.endsWith(`/${path}`);
        return (
          <ListGroupItem
            key={label}
            as={Link}
            href={href}
            className={`bg-white border-0 text-decoration-none ${
              active
                ? "text-dark border-start border-2 border-danger ps-2"
                : "text-danger"
            }`}
          >
            {label}
          </ListGroupItem>
        );
      })}
    </ListGroup>
  );
}
