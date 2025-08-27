"use client";

import { useEffect, useState } from "react";
import {
  Avatar,
  Badge,
  Button,
  Card,
  Container,
  Group,
  Loader,
  Notification,
  Stack,
  Text,
  Title,
} from "@mantine/core";
import { useSelector } from "react-redux";
import { useRouter } from "next/navigation";

export default function AppointmentsPage() {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const userData = useSelector((state) => state.user.data);
  const router = useRouter();

  useEffect(() => {
    async function fetchAppointments() {
      try {
        let res;
        if (userData.role !== "DOCTOR") {
          res = await fetch(
            `http://gateway:8090/v1/appointments/patients/${userData.id}`,
            {
              method: "GET",
              headers: { "Content-Type": "application/json" },
            }
          );
        } else {
          res = await fetch(
            `http://gateway:8090/v1/appointments/doctors/${userData.id}`,
            {
              method: "GET",
              headers: { "Content-Type": "application/json" },
            }
          );
        }

        if (!res.ok) setError("Failed to fetch appointments");
        const data = await res.json();
        console.log("Fetched appointments:", data);
        setAppointments(data ?? []);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    fetchAppointments();
  }, [userData]);

  if (loading) {
    return (
      <Container
        size="sm"
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <Loader color="#1e40af" size="lg" />
      </Container>
    );
  }

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
            title="Internal Error"
            withCloseButton
            onClose={() => setError(null)}
          >
            {error}
          </Notification>
        </div>
      )}
      <Container size="md" py="xl">
        <Title order={2} mb="lg" style={{ color: "#1e40af" }}>
          All Appointments
        </Title>

        {appointments.length === 0 ? (
          <Text color="dimmed">No appointments found.</Text>
        ) : (
          <Stack spacing="md">
            {appointments.map((appt) => {
              const otherId =
                userData.role === "DOCTOR" ? appt.patientId : appt.doctorId;

              return (
                <Card
                  key={appt.appointmentId}
                  shadow="sm"
                  radius="md"
                  withBorder
                >
                  <Group position="apart" align="center">
                    <Group>
                      <Avatar
                        name={
                          userData.role === "DOCTOR"
                            ? appt.patientName
                            : appt.doctorName
                        }
                        color="#1e40af"
                        radius="xl"
                      />
                      <div>
                        <Text fw={600}>
                          {userData.role === "DOCTOR"
                            ? `Patient: ${appt.patientName}`
                            : `Doctor: ${appt.doctorName}`}
                        </Text>
                        <Text size="sm" color="dimmed">
                          Date: {appt.date} • {appt.timeSlot}
                        </Text>
                      </div>
                    </Group>

                    <Group spacing="xs">
                      <Badge
                        color={
                          appt.status === "APPROVED"
                            ? "green"
                            : appt.status === "CANCELLED"
                            ? "red"
                            : "gray"
                        }
                      >
                        {appt.status}
                      </Badge>

                      <Button
                        size="xs"
                        color="#1e40af"
                        variant="light"
                        onClick={() =>
                          router.push(`/message/${userData.id}/${otherId}`)
                        }
                      >
                        Chat
                      </Button>
                      <Button
                        size="xs"
                        color="#1e40af"
                        onClick={() =>
                          router.push(`/videochat/${userData.id}/${otherId}`)
                        }
                      >
                        Video Call
                      </Button>
                    </Group>
                  </Group>
                </Card>
              );
            })}
          </Stack>
        )}
      </Container>
    </>
  );
}
