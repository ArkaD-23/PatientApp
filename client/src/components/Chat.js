"use client";

import { useState, useEffect } from "react";
import {
  Paper,
  TextInput,
  Button,
  ScrollArea,
  Group,
  Text,
} from "@mantine/core";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

export default function Chat({ senderId, recipientId }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [stompClient, setStompClient] = useState(null);

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8082/ws-chat"),
      reconnectDelay: 5000,
    });

    client.onConnect = async () => {

      console.log("Connected to WebSocket");

      // 1. load history first
      const res = await fetch(
        `http://localhost:8082/v1/chat/messages/${senderId}/${recipientId}`
      );
      const data = await res.json();
      setMessages(data);
    
      // 2. then subscribe
      client.subscribe(`/topic/messages/${senderId}`, (msg) => {
        console.log("📩 Incoming raw:", msg.body);
        const body = JSON.parse(msg.body);
        setMessages((prev) => [...prev, body]);
      });
    };
    

    console.log("messages: ", messages);

    client.activate();
    setStompClient(client);

    return () => client.deactivate();
  }, []);

  const sendMessage = () => {
    if (!input.trim() || !stompClient) return;

    const chatMessage = {
      senderId: senderId,
      recipientId: recipientId,
      content: input,
      type: "TEXT",
      ts: Date.now(),
    };

    // broadcast via WS
    stompClient.publish({
      destination: "/app/chat.send",
      body: JSON.stringify(chatMessage),
    });
    console.log("📤 Sent:", chatMessage);

    setInput("");
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
          {Array.isArray(messages) ? messages.map((msg, i) => (
              <div
                key={i}
                style={{
                  alignSelf:
                    msg.senderId === senderId ? "flex-end" : "flex-start",
                  backgroundColor:
                    msg.senderId === senderId ? "#1e40af" : "#e5e7eb",
                  color: msg.senderId === senderId ? "white" : "black",
                  padding: "8px 12px",
                  borderRadius: 12,
                  maxWidth: "70%",
                }}
              >
                <Text size="sm">{msg.content}</Text>
              </div>
            )) : null}
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
