"use client";

import Link from "next/link";
import { useSelector } from "react-redux";
import { usePathname } from "next/navigation";

export default function AccountNavigation() {
  const { currentUser } = useSelector((state: any) => state.account);
  const pathname = usePathname();

  return (
    <div id="wd-account-nav" style={{ width: 140 }}>
      <ul className="list-unstyled m-0 p-0">

        <li className="py-2">
          <Link
            href="/Kambaz/Account/Signin"
            className="d-block px-2 fw-semibold text-dark border-start border-3 border-dark"
          >
            Signin
          </Link>
        </li>

        <li className="py-2">
          <Link
            href="/Kambaz/Account/Signup"
            className="d-block px-2 text-danger text-decoration-none"
          >
            Signup
          </Link>
        </li>

        <li className="py-2">
          <Link
            href="/Kambaz/Account/Profile"
            className="d-block px-2 text-danger text-decoration-none"
          >
            Profile
          </Link>
        </li>

        {currentUser && currentUser.role === "ADMIN" && (
          <li className="py-2">
            <Link
              href="/Kambaz/Account/Users"
              className={`d-block px-2 text-danger text-decoration-none ${
                pathname.endsWith("Users") ? "fw-bold border-start border-3 border-danger" : ""
              }`}
            >
              Users
            </Link>
          </li>
        )}

      </ul>
    </div>
  );
}
