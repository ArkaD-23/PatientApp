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
import { useRouter } from "next/navigation";
import { useSelector } from "react-redux";

export default function DoctorsList() {
  const [doctors, setDoctors] = useState([]);
  const router = useRouter();
  const user = useSelector((state) => state.user.data);

  const fetchDoctors = async () => {

    const res = await fetch("http://localhost:8082/v1/users/doctors", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await res.json();
    console.log("Doctors data:", data);

    setDoctors(data);
  }

  useEffect(() => {
    fetchDoctors();
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
                <Avatar name={doc.email} color="#1e40af" radius="xl"/>
                <div>
                  <Text weight={600}>{doc.email}</Text>
                  {/* <Text size="sm" color="dimmed">
                    {doc.specialty}
                  </Text> */}
                </div>
              </Group>

              <Group spacing="xs">
                <Button
                  size="xs"
                  color="#1e40af"
                  variant="light"
                  onClick={() => router.push(`/message/${user.id}/${doc.id}`)}
                >
                  Chat
                </Button>
                <Button
                  size="xs"
                  color="#1e40af"
                  onClick={() => router.push(`/videochat/${user.id}/${doc.id}`)}
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
