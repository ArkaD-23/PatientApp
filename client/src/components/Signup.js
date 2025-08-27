"use client";

import {
  Button,
  Title,
  Stack,
  TextInput,
  PasswordInput,
  Paper,
  Select,
} from "@mantine/core";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function SignUpPage() {
  const router = useRouter();
  const [fullname, setFullname] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [role, setRole] = useState("PATIENT"); // default role
  const [loading, setLoading] = useState(false);

  const handleSignup = async () => {
    if (password !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    setLoading(true);
    try {
      const res = await fetch("http://gateway:8090/v1/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          fullname,
          email,
          password,
          role, // send selected role
        }),
      });

      if (!res.ok) throw new Error("Signup failed");

      const data = await res.json();
      console.log("✅ Signup Response:", data);

      if (data.status) {
        alert("Account created! Please login.");
        router.push("/signin");
      } else {
        alert("Signup failed");
      }
    } catch (err) {
      console.error(err);
      alert("Error while signing up");
    } finally {
      setLoading(false);
    }
  };

  return (
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
          Sign Up
        </Title>
        <Stack>
          <TextInput
            label="Full Name"
            placeholder="John Doe"
            required
            value={fullname}
            onChange={(e) => setFullname(e.currentTarget.value)}
          />
          <TextInput
            label="Email"
            placeholder="you@example.com"
            required
            value={email}
            onChange={(e) => setEmail(e.currentTarget.value)}
          />
          <PasswordInput
            label="Password"
            placeholder="Create a password"
            required
            value={password}
            onChange={(e) => setPassword(e.currentTarget.value)}
          />
          <PasswordInput
            label="Confirm Password"
            placeholder="Confirm your password"
            required
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.currentTarget.value)}
          />
          {/* Role Selector */}
          <Select
            label="Role"
            placeholder="Select your role"
            data={[
              { value: "PATIENT", label: "Patient" },
              { value: "DOCTOR", label: "Doctor" },
            ]}
            value={role}
            onChange={(value) => setRole(value || "PATIENT")}
            required
          />

          <Button
            fullWidth
            color="#1e40af"
            loading={loading}
            onClick={handleSignup}
          >
            Sign Up
          </Button>

          <p>
            Already have an account?{" "}
            <Link href="/signin">
              <span style={{ color: "#1e40af" }}>Signin</span>
            </Link>
          </p>
        </Stack>
      </Paper>
    </div>
  );
}
