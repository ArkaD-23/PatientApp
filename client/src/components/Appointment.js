"use client";

import { useState } from "react";
import {
  Paper,
  Title,
  Text,
  Button,
  Select,
  Stack,
  Group,
  Container,
} from "@mantine/core";
import { DatePicker } from "@mantine/dates";

export default function AppointmentBooking() {
  const [doctor, setDoctor] = useState("");
  const [date, setDate] = useState(null);
  const [time, setTime] = useState("");

  const handleBooking = () => {
    if (!doctor || !date || !time) {
      alert("Please select all fields before booking.");
      return;
    }
    alert(
      `✅ Appointment booked with ${doctor} on ${date.toDateString()} at ${time}`
    );
  };

  return (
    <Container
      size="sm"
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Paper
        shadow="md"
        radius="lg"
        p="xl"
        withBorder
        style={{ width: "100%" }}
      >
        <Title order={2} align="center" mb="md" style={{ color: "#1e3a8a" }}>
          Book an Appointment
        </Title>
        <Stack spacing="md">
          {/* Doctor Selection */}
          <Select
            label="Select Doctor"
            placeholder="Choose a doctor"
            data={[
              {
                value: "Dr. John Smith",
                label: "Dr. John Smith - Cardiologist",
              },
              {
                value: "Dr. Emily Carter",
                label: "Dr. Emily Carter - Dentist",
              },
              {
                value: "Dr. Alex Brown",
                label: "Dr. Alex Brown - Dermatologist",
              },
            ]}
            value={doctor}
            onChange={setDoctor}
            required
          />

          {/* Date Picker */}
          <DatePicker
            label="Select Date"
            placeholder="Pick a date"
            value={date}
            onChange={setDate}
            required
          />

          {/* Time Slots */}
          <Select
            label="Select Time"
            placeholder="Choose a time"
            data={[
              "09:00 AM",
              "10:30 AM",
              "12:00 PM",
              "02:00 PM",
              "03:30 PM",
              "05:00 PM",
            ]}
            value={time}
            onChange={setTime}
            required
          />

          {/* Book Button */}
          <Group position="center" mt="md">
            <Button
              color="#1e3a8a"
              radius="xl"
              size="md"
              onClick={handleBooking}
            >
              Book Appointment
            </Button>
          </Group>
        </Stack>
      </Paper>
    </Container>
  );
}
