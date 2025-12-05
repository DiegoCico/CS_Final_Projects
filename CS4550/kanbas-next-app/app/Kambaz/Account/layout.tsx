import { ReactNode } from "react";
import AccountNavigation from "./Navigation";

export default function AccountLayout({
  children,
}: Readonly<{ children: ReactNode }>) {
  return (
    <div id="wd-kambaz">
      <table className="w-100">
        <tbody>
          <tr>
            <td valign="top" style={{ width: 180, paddingRight: 24 }}>
              <AccountNavigation />
            </td>

            <td valign="top" width="100%" className="p-3">
              {children}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
}
