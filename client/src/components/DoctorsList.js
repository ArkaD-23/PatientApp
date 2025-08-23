"use client";

import { useState, useEffect } from "react";
import {
  Avatar,
  Button,
  Card,
  Container,
  Group,
  Stack,
  Text,
  Title,
} from "@mantine/core";

export default function DoctorsList() {
  const [doctors, setDoctors] = useState([]);

  useEffect(() => {
    setDoctors([
      { id: 1, name: "Alice Johnson", specialty: "Cardiologist" },
      { id: 2, name: "Brian Smith", specialty: "Dermatologist" },
      { id: 3, name: "Clara White", specialty: "Pediatrician" },
      { id: 4, name: "David Lee", specialty: "Neurologist" },
    ]);
  }, []);

  return (
    <Container size="md" py="xl">
      <Title order={2} mb="lg" style={{ color: "#1e40af" }}>
        Our Doctors
      </Title>

      <Stack spacing="md">
        {doctors.map((doc) => (
          <Card key={doc.id} shadow="sm" radius="md" withBorder>
            <Group position="apart">
              <Group>
                <Avatar color="blue" radius="xl">
                  {doc.name
                    .split(" ")
                    .map((n) => n[0])
                    .join("")
                    .toUpperCase()}
                </Avatar>
                <div>
                  <Text weight={600}>{doc.name}</Text>
                  <Text size="sm" color="dimmed">
                    {doc.specialty}
                  </Text>
                </div>
              </Group>

              <Group spacing="xs">
                <Button
                  size="xs"
                  color="blue"
                  variant="light"
                  onClick={() => alert(`Chat with ${doc.name}`)}
                >
                  Chat
                </Button>
                <Button
                  size="xs"
                  color="green"
                  onClick={() => alert(`Video call with ${doc.name}`)}
                >
                  Video Call
                </Button>
              </Group>
            </Group>
          </Card>
        ))}
      </Stack>
    </Container>
  );
}
