"use client";
import { usePathname } from "next/navigation";

export default function Breadcrumb({
  course,
}: {
  course?: { name: string };
}) {
  const pathname = usePathname();
  const current = decodeURIComponent(pathname.split("/").pop() ?? "");
  return (
    <span className="text-danger">
      {course?.name} &gt; {current}
    </span>
  );
}
