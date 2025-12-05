/* eslint-disable */
"use client";

import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { setCurrentUser } from "../reducer";
import { Button, FormControl } from "react-bootstrap";
import * as client from "../client";


export default function Profile() {
  const [profile, setProfile] = useState<any>({});
  const dispatch = useDispatch();
  const router = useRouter();

  const { currentUser } = useSelector((state: any) => state.account);
    const updateProfile = async () => {
    const updatedProfile = await client.updateUser(profile);
    dispatch(setCurrentUser(updatedProfile));
  };


  useEffect(() => {
    const savedUser = localStorage.getItem("currentUser");
    if (!currentUser && savedUser) {
      dispatch(setCurrentUser(JSON.parse(savedUser)));
      setProfile(JSON.parse(savedUser));
    } else if (currentUser) {
      setProfile(currentUser);
    } else {
      router.push("/Kambaz/Account/Signin");
    }
  }, [currentUser, dispatch, router]);

  const handleChange = (field: string, value: string) => {
    setProfile({ ...profile, [field]: value });
  };

  const saveProfile = () => {
    dispatch(setCurrentUser(profile));
    localStorage.setItem("currentUser", JSON.stringify(profile));
    alert("Profile updated successfully!");
  };

  const signout = async () => {
    await client.signout();
    dispatch(setCurrentUser(null));
    localStorage.removeItem("currentUser");
    router.push("/Kambaz/Account/Signin");
  };

  if (!profile?.username) return <div>Loading profile...</div>;

  return (
    <div className="wd-profile-screen mx-auto" style={{ maxWidth: 400 }}>
      <h3 className="mb-3">Profile</h3>

      <FormControl
        id="wd-username"
        className="mb-2"
        value={profile.username || ""}
        onChange={(e) => handleChange("username", e.target.value)}
      />

      <FormControl
        id="wd-password"
        type="password"
        className="mb-2"
        value={profile.password || ""}
        onChange={(e) => handleChange("password", e.target.value)}
      />

      <FormControl
        id="wd-firstname"
        className="mb-2"
        value={profile.firstName || ""}
        onChange={(e) => handleChange("firstName", e.target.value)}
      />

      <FormControl
        id="wd-lastname"
        className="mb-2"
        value={profile.lastName || ""}
        onChange={(e) => handleChange("lastName", e.target.value)}
      />

      <FormControl
        id="wd-dob"
        type="date"
        className="mb-2"
        value={profile.dob || ""}
        onChange={(e) => handleChange("dob", e.target.value)}
      />

      <FormControl
        id="wd-email"
        className="mb-2"
        value={profile.email || ""}
        onChange={(e) => handleChange("email", e.target.value)}
      />

      <select
        className="form-control mb-3"
        id="wd-role"
        value={profile.role || "STUDENT"}
        onChange={(e) => handleChange("role", e.target.value)}
      >
        <option value="USER">User</option>
        <option value="ADMIN">Admin</option>
        <option value="FACULTY">Faculty</option>
        <option value="STUDENT">Student</option>
      </select>

      <div className="d-flex flex-column gap-2">
        <Button
          variant="primary"
          className="w-100"
          onClick={saveProfile}
          id="wd-save-profile"
        >
          Save Changes
        </Button>
        <button onClick={updateProfile} className="btn btn-primary w-100 mb-2"> Update </button>
        <Button
          variant="danger"
          className="w-100"
          onClick={signout}
          id="wd-signout-btn"
        >
          Sign Out
        </Button>
      </div>
    </div>
  );
}
