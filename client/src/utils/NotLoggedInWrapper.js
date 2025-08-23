"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Loader } from "@mantine/core";

export default function NotLoggedInRequired({ children }) {
  const router = useRouter();
  const [checking, setChecking] = useState(true);
  const [authorized, setAuthorized] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (token) {
      router.replace("/");
      setAuthorized(true);
    }

    setChecking(false);
  }, [router]);

  if (checking) {
    return (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <Loader size="lg" color="#1e40af" />
      </div>
    );
  }

  if (!authorized) {
    return <>{children}</>;
  }

  return null;
}
