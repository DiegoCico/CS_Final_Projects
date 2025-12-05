"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import PeopleTable from "./Table/PeopleTable";
import * as client from "../../client";

export default function CoursePeoplePage() {
  const { cid } = useParams();
  const [users, setUsers] = useState([]);

  const fetchUsers = async () => {
    const data = await client.findUsersForCourse(cid as string);
    setUsers(data);
  };

  useEffect(() => {
    fetchUsers();
  }, [cid]);

  return (
    <div>
      <h3>People Enrolled in This Course</h3>
      <PeopleTable users={users} fetchUsers={fetchUsers} />
    </div>
  );
}
