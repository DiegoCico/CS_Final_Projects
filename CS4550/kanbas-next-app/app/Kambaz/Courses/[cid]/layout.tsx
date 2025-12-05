"use client";
import { ReactNode } from "react";
import CourseNavigation from "./Navigation";
import { useSelector } from "react-redux";
import { useParams } from "next/navigation";
import { FaAlignJustify } from "react-icons/fa6";
export default function CoursesLayout({ children }: { children: ReactNode }) {
 const params = useParams();
 const rawCid = params?.cid;
 const cid = Array.isArray(rawCid) ? rawCid[0] : (rawCid ?? "");
 const { courses } = useSelector((state: any) => state.courses);
 const course = courses.find((course: any) => course._id === cid);
 return (
   <div id="wd-courses">
     <h2>
       <FaAlignJustify className="me-4 fs-4 mb-1" />
       {course?.name}
     </h2>
     <hr />
     <div className="d-flex">
       <div>
         <CourseNavigation cid={cid} />
                </div>
       <div className="flex-fill">{children}</div>
     </div>
   </div>
 );
}
