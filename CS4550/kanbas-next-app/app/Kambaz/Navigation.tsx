"use client";
import { ListGroup, ListGroupItem } from "react-bootstrap";
import Link from "next/link";
import { usePathname } from "next/navigation";

import { AiOutlineDashboard } from "react-icons/ai";
import { IoCalendarOutline } from "react-icons/io5";
import { LiaBookSolid, LiaCogSolid } from "react-icons/lia";
import { FaInbox, FaRegCircleUser } from "react-icons/fa6";
import { useSelector } from "react-redux";


const LINKS = [
  { label: "Dashboard", path: "/Kambaz/Dashboard", id: "wd-dashboard-link", icon: "AiOutlineDashboard" },
  { label: "Courses",   path: "/Kambaz/Courses",   id: "wd-course-link",    icon: "LiaBookSolid" },
  { label: "Calendar",  path: "/Calendar",         id: "wd-calendar-link",  icon: "IoCalendarOutline" },
  { label: "Inbox",     path: "/Inbox",            id: "wd-inbox-link",     icon: "FaInbox" },
  { label: "Labs",      path: "/Labs",             id: "wd-labs-link",      icon: "LiaCogSolid" },
] as const;

const ICONS = {
  AiOutlineDashboard,
  LiaBookSolid,
  IoCalendarOutline,
  FaInbox,
  LiaCogSolid,
  FaRegCircleUser,
} as const;

export default function KambazNavigation() {
    const { currentUser } = useSelector((state: any) => state.account);
  const links = currentUser ? ["Profile"] : ["Signin", "Signup"];
  const pathname = usePathname();

  const isActive = (href: string) =>
    pathname === href || pathname.startsWith(`${href}/`);

  return (
    <ListGroup
      id="wd-kambaz-navigation"
      className="rounded-0 position-fixed bottom-0 top-0 d-none d-md-block bg-black z-2"
      style={{ width: 120 }}
    >
      <ListGroupItem
        className="bg-black border-0 text-center"
        as="a"
        href="https://www.northeastern.edu/"
        id="wd-neu-link"
        target="_blank"
        rel="noreferrer"
      >
        <img src="/NEU.png" width="75px" alt="Northeastern University" />
      </ListGroupItem>

      <ListGroupItem
        as={Link}
        href="/Kambaz/Account/Profile"
        id="wd-account-link"
        className={`text-center border-0 ${
          isActive("/Kambaz/Account") ? "bg-white text-danger" : "bg-black text-white"
        }`}
      >
        <ICONS.FaRegCircleUser
          className={`fs-1 ${isActive("/Kambaz/Account") ? "text-danger" : "text-white"}`}
        />
        <br />
        Account
      </ListGroupItem>

      {LINKS.map((link) => {
        const Icon = ICONS[link.icon as keyof typeof ICONS];
        const active = isActive(link.path);
        return (
          <ListGroupItem
            key={link.id}
            as={Link}
            href={link.path}
            id={link.id}
            className={`text-center border-0 ${active ? "bg-white text-danger" : "bg-black text-white"}`}
          >
            <Icon className={`fs-1 ${active ? "text-danger" : "text-white"}`} />
            <br />
            {link.label}
          </ListGroupItem>
        );
      })}
    </ListGroup>
  );
}
