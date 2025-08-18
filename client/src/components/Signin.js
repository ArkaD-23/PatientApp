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
import { useRouter } from "next/navigation";

export default function SignInPage() {
  const router = useRouter();
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
          Sign In
        </Title>
        <Stack>
          <TextInput label="Email" placeholder="you@example.com" required />
          <PasswordInput
            label="Password"
            placeholder="Enter your password"
            required
          />
          <Button fullWidth color="#1e40af">
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
  );
}
