"use client";

import {
  Button,
  Container,
  Title,
  Text,
  Grid,
  Card,
  Group,
  Flex,
} from "@mantine/core";
import { Calendar, Phone, User } from "lucide-react";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();
  return (
    <div>
      {/* Hero Section */}
      <Container size="lg" py={80}>
        <div style={{ textAlign: "center" }}>
          <Title order={1} size="h1" weight={700} style={{ color: "#1e3a8a" }}>
            Book Your Doctor Appointment Easily
          </Title>
          <Text
            mt="md"
            size="lg"
            color="dimmed"
            style={{ maxWidth: 600, margin: "0 auto" }}
          >
            Find trusted doctors, view their availability, and schedule your
            visit in just a few clicks.
          </Text>
          <Flex mt="xl" justify="center" gap="md">
            <Button onClick={() => router.push("/appointment")} size="md" color="#1e40af" radius="xl">
              Book Appointment
            </Button>
            <Button size="md" color="#1e40af" variant="outline" radius="xl">
              Learn More
            </Button>
          </Flex>
        </div>
      </Container>

      {/* Features Section */}
      <Container size="lg" py={60}>
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            gap: "24px",
            flexWrap: "wrap", // ensures they stack on small screens
          }}
        >
          <Card
            shadow="sm"
            radius="xl"
            padding="lg"
            withBorder
            style={{ width: 280 }}
          >
            <div style={{ textAlign: "center" }}>
              <User size={48} color="#1e3a8a" style={{ margin: "0 auto" }} />
              <Title order={3} mt="md">
                Find a Doctor
              </Title>
              <Text mt="sm" color="dimmed">
                Browse top-rated doctors by specialty and location.
              </Text>
            </div>
          </Card>

          <Card
            shadow="sm"
            radius="xl"
            padding="lg"
            withBorder
            style={{ width: 280 }}
          >
            <div style={{ textAlign: "center" }}>
              <Calendar
                size={48}
                color="#1e3a8a"
                style={{ margin: "0 auto" }}
              />
              <Title order={3} mt="md">
                Easy Scheduling
              </Title>
              <Text mt="sm" color="dimmed">
                Check availability and book instantly online.
              </Text>
            </div>
          </Card>

          <Card
            shadow="sm"
            radius="xl"
            padding="lg"
            withBorder
            style={{ width: 280 }}
          >
            <div style={{ textAlign: "center" }}>
              <Phone size={48} color="#1e3a8a" style={{ margin: "0 auto" }} />
              <Title order={3} mt="md">
                24/7 Support
              </Title>
              <Text mt="sm" color="dimmed">
                Get help whenever you need with round-the-clock assistance.
              </Text>
            </div>
          </Card>
        </div>
      </Container>

      {/* CTA Section */}
      <div
        style={{
          backgroundColor: "#1e3a8a",
          color: "white",
          padding: "80px 20px",
          textAlign: "center",
        }}
      >
        <Container size="sm">
          <Title order={2} size="h2" weight={700}>
            Ready to Book Your Appointment?
          </Title>
          <Text mt="md" size="lg" color="blue.1">
            Take the first step towards better health. Schedule a consultation
            today.
          </Text>
          <Button
            mt="xl"
            size="md"
            color="white"
            variant="filled"
            radius="xl"
            style={{ color: "#2563eb" }}
            onClick={() => router.push("/signup")}
          >
            Get Started
          </Button>
        </Container>
      </div>

      {/* Footer */}
      <Container size="lg" py={40} style={{ textAlign: "center" }}>
        <Text size="sm" color="dimmed">
          © {new Date().getFullYear()} MediBridge. All rights reserved.
        </Text>
      </Container>
    </div>
  );
}
