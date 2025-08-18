"use client";

import { Button, Container, Title, Text, Grid, Card, Group, Paper, Stack } from "@mantine/core";
import { Calendar, Phone, User } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";

export default function () {
  const router = useRouter();
  return (
    <div style={{ backgroundColor: "#f9fafb" }}>
      {/* Navbar */}
      <Paper radius={0} py={20} withBorder style={{ backgroundColor: "white", borderBottom: "1px solid #e5e7eb" }}>
        <Container size="lg" style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <Link href="/"> 
            <Title order={3} style={{ color: "#1e40af" }}>MediBridge</Title>
          </Link>
          <Group spacing="xl">
            <Text component="a" href="#" weight={500} style={{ color: "#1f2937", cursor: "pointer" }}>Home</Text>
            <Text component="a" href="#features" weight={500} style={{ color: "#1f2937", cursor: "pointer" }}>Features</Text>
            <Text component="a" href="#about" weight={500} style={{ color: "#1f2937", cursor: "pointer" }}>About</Text>
            <Text component="a" href="#contact" weight={500} style={{ color: "#1f2937", cursor: "pointer" }}>Contact</Text>
            <Button size="sm" color="#1e40af" radius="xl" onClick={() => router.push("/signin")}>Signin</Button>
          </Group>
        </Container>
      </Paper>
    </div>
  );
}