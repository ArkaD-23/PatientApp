"use client";

import { useEffect, useState } from "react";
import {
  Paper,
  Title,
  Text,
  Button,
  Select,
  Stack,
  Group,
  Container,
  Notification,
} from "@mantine/core";
import { DatePicker } from "@mantine/dates";
import { useSelector } from "react-redux";

export default function AppointmentBooking() {
  const [doctorId, setDoctorId] = useState("");
  const [date, setDate] = useState(null);
  const [timeSlot, setTimeSlot] = useState("");
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [doctors, setDoctors] = useState([]);

  const patientId = useSelector((state) => state.user.data?.id);

  const fetchDoctors = async () => {

    const res = await fetch("http://gateway:8090/v1/users/doctors", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await res.json();
    console.log("Doctors data:", data);

    const doctorOptions = data.map((doc) => ({
      value: doc.id, 
      label: doc.email,
    }));

    setDoctors(doctorOptions);
  }

  useEffect(() => {
    fetchDoctors();
  }, []);

  const handleBooking = async () => {
    setLoading(true);
    if (!doctorId || !date || !timeSlot) {
      setError("Please select all fields before booking.");
      setLoading(false);
      return;
    }
    const res = await fetch("http://gateway:8090/v1/appointments/book", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        patientId,
        doctorId,
        timeSlot,
        date,
      }),
    });
    console.log("Booking response:", res);
    if (res.ok) {
      setSuccess(`Appointment booked on ${date} at ${timeSlot}`);
      setLoading(false);
      return;
    }
    const errorData = await res.json();
    setError(errorData.message || "Failed to book appointment.");
    setLoading(false);
    return;
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
            title="Booking Failed"
            withCloseButton
            onClose={() => setError(null)}
          >
            {error}
          </Notification>
        </div>
      )}
      {success && (
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
            color="green"
            title="Booking Successful"
            withCloseButton
            onClose={() => setSuccess(null)}
          >
            {success}
          </Notification>
        </div>
      )}
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
            <Select
              label="Select Doctor"
              placeholder="Choose a doctor"
              data={doctors}
              value={doctorId}
              onChange={setDoctorId}
              required
            />

            <DatePicker
              label="Select Date"
              placeholder="Pick a date"
              value={date}
              onChange={setDate}
              required
            />

            <Select
              label="Select Time"
              placeholder="Choose a timeSlot"
              data={[
                "09:00 AM",
                "10:30 AM",
                "12:00 PM",
                "02:00 PM",
                "03:30 PM",
                "05:00 PM",
              ]}
              value={timeSlot}
              onChange={setTimeSlot}
              required
            />

            <Group position="center" mt="md">
              <Button
                color="#1e3a8a"
                radius="xl"
                size="md"
                onClick={handleBooking}
                loading={loading}
              >
                Book Appointment
              </Button>
            </Group>
          </Stack>
        </Paper>
      </Container>
    </>
  );
}
