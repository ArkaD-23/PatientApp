"use client";

import { useState } from "react";
import { Paper, TextInput, Button, ScrollArea, Group, Text } from "@mantine/core";

export default function Chat() {
  const [messages, setMessages] = useState([
    { from: "doctor", text: "Hello! How can I help you today?" },
    { from: "user", text: "I want to book an appointment for a checkup." },
  ]);
  const [input, setInput] = useState("");

  const sendMessage = () => {
    if (!input.trim()) return;
    setMessages([...messages, { from: "user", text: input }]);
    setInput("");
    // Simulate doctor reply
    setTimeout(() => {
      setMessages((prev) => [
        ...prev,
        { from: "doctor", text: "Sure, please pick a date and time." },
      ]);
    }, 1000);
  };

  return (
    <div
      style={{
        backgroundColor: "#f9fafb",
        height: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Paper
        shadow="md"
        p="md"
        style={{
          width: 600,
          height: "100vh",
          display: "flex",
          flexDirection: "column",
        }}
      >
        {/* Messages */}
        <ScrollArea style={{ flex: 1, marginBottom: "10px" }}>
          <div style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
            {messages.map((msg, i) => (
              <div
                key={i}
                style={{
                  alignSelf: msg.from === "user" ? "flex-end" : "flex-start",
                  backgroundColor: msg.from === "user" ? "#1e40af" : "#e5e7eb",
                  color: msg.from === "user" ? "white" : "black",
                  padding: "8px 12px",
                  borderRadius: 12,
                  maxWidth: "70%",
                }}
              >
                <Text size="sm">{msg.text}</Text>
              </div>
            ))}
          </div>
        </ScrollArea>

        {/* Input Area */}
        <Group>
          <TextInput
            placeholder="Type a message..."
            value={input}
            onChange={(e) => setInput(e.currentTarget.value)}
            style={{ flex: 1 }}
          />
          <Button onClick={sendMessage} color="#1e40af">
            Send
          </Button>
        </Group>
      </Paper>
    </div>
  );
}
