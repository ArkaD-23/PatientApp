"use client";

import {
  Button,
  Container,
  Title,
  Text,
  Stack,
  TextInput,
  PasswordInput,
  Paper,
} from "@mantine/core";
import Link from "next/link";

export default function SignUpPage() {
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
          <TextInput label="Full Name" placeholder="John Doe" required />
          <TextInput label="Email" placeholder="you@example.com" required />
          <PasswordInput
            label="Password"
            placeholder="Create a password"
            required
          />
          <PasswordInput
            label="Confirm Password"
            placeholder="Confirm your password"
            required
          />
          <Button fullWidth color="#1e40af">
            Sign Up
          </Button>
          <p>
            Alredy have an account ?{" "}
            <Link href="/signin">
              <span style={{ color: "#1e40af" }}>Signin</span>
            </Link>
          </p>
        </Stack>
      </Paper>
    </div>
  );
}
