"use client";
import { useSelector } from "react-redux";
import { redirect } from "next/dist/client/components/navigation";


export default function AccountPage() {
 const { currentUser } = useSelector((state: any) => state.account);
 if (!currentUser) {
   redirect("/Kambaz/Account/Signin");
 } else {
   redirect("/Kambaz/Account/Profile");
 }
}
