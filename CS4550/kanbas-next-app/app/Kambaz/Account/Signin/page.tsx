"use client";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { setCurrentUser } from "../reducer";
import { useDispatch } from "react-redux";
import { useState } from "react";
import * as db from "../../Database";
import { FormControl, Button } from "react-bootstrap";
import * as client from "../client";

export default function Signin() {
  const [credentials, setCredentials] = useState<any>({});
  const dispatch = useDispatch();
  const router = useRouter();

  const signin = async () => {
    const user = await client.signin(credentials);

    if (!user) {
      alert("Invalid username or password");
      return;
    }

    dispatch(setCurrentUser(user));
    localStorage.setItem("currentUser", JSON.stringify(user));

    router.push("/Kambaz/Dashboard");
  };

  return (
    <div id="wd-signin-screen" className="mx-auto" style={{ maxWidth: 360 }}>
      <h4 className="mb-3">Signin</h4>

      <FormControl
        value={credentials.username || ""}
        onChange={(e) =>
          setCredentials({ ...credentials, username: e.target.value })
        }
        id="wd-username"
        placeholder="username"
        className="mb-2"
      />

      <FormControl
        value={credentials.password || ""}
        onChange={(e) =>
          setCredentials({ ...credentials, password: e.target.value })
        }
        id="wd-password"
        type="password"
        placeholder="password"
        className="mb-2"
      />

      <Button
        onClick={signin}
        id="wd-signin-btn"
        className="btn btn-primary w-100 mb-2"
      >
        Signin
      </Button>

      <Link id="wd-signup-link" href="/Kambaz/Account/Signup">
        Signup
      </Link>
    </div>
  );
}
