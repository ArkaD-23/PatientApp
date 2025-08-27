"use client";

import { fetchUser } from "@/lib/features/userSlice";
import {
  Button,
  Title,
  Stack,
  TextInput,
  PasswordInput,
  Paper,
  Notification,
} from "@mantine/core";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { useDispatch } from "react-redux";

export default function SignInPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const dispatch = useDispatch();

  const handleLogin = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch("http://gateway:8090/v1/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (!res.ok) throw new Error("Login failed");

      const data = await res.json();
      console.log("✅ Login Response:", data);
      dispatch(fetchUser(email, data.token));

      localStorage.setItem("token", data.token);

      if (data.success === true) {
        window.location.href = "/";
      } else {
        setError("Please give correct credentials !");
      }
    } catch (err) {
      console.error(err);
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {error && (
        <div
          style={{
            position: "fixed",
            bottom: 20,
            right: 20,
            maxWidth: "350px",
            zIndex: 2000,
          }}
        >
          <Notification
            color="red"
            title="Login Failed"
            withCloseButton
            onClose={() => setError(null)}
          >
            {error}
          </Notification>
        </div>
      )}

      <div
        style={{
          backgroundColor: "#f9fafb",
          minHeight: "100vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Paper radius="md" p="xl" shadow="md" style={{ width: 400 }}>
          <Title order={2} align="center" mb="lg">
            Sign In
          </Title>

          <Stack>
            <TextInput
              label="Email"
              placeholder="you@example.com"
              required
              value={email}
              onChange={(e) => setEmail(e.currentTarget.value)}
            />
            <PasswordInput
              label="Password"
              placeholder="Enter your password"
              required
              value={password}
              onChange={(e) => setPassword(e.currentTarget.value)}
            />
            <Button
              fullWidth
              color="#1e40af"
              loading={loading}
              onClick={handleLogin}
            >
              Sign In
            </Button>
            <p>
              Do not have an account?{" "}
              <Link href="/signup">
                <span style={{ color: "#1e40af" }}>Signup</span>
              </Link>
            </p>
          </Stack>
        </Paper>
      </div>
    </>
  );
}
