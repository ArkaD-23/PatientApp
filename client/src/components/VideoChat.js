"use client";

import { useState } from "react";
import { Paper, Button, Group } from "@mantine/core";
import { Mic, MicOff, Video, VideoOff, PhoneOff } from "lucide-react";

export default function VideoCall() {
  const [micOn, setMicOn] = useState(true);
  const [videoOn, setVideoOn] = useState(true);

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
      <Paper
        shadow="lg"
        radius="md"
        p="md"
        style={{
          width: "90%",
          maxWidth: "900px",
          height: "80vh",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          position: "relative",
          overflow: "hidden",
        }}
      >
        {/* Doctor Video (Main feed) */}
        <div
          style={{
            flex: 1,
            backgroundColor: "#000",
            borderRadius: "8px",
            position: "relative",
          }}
        >
          <video
            autoPlay
            playsInline
            muted
            style={{
              width: "100%",
              height: "100%",
              objectFit: "cover",
              borderRadius: "8px",
            }}
          />
          <div
            style={{
              position: "absolute",
              bottom: "10px",
              left: "10px",
              backgroundColor: "rgba(0,0,0,0.6)",
              color: "white",
              padding: "4px 8px",
              borderRadius: "6px",
              fontSize: "14px",
            }}
          >
            Dr. Smith
          </div>
        </div>

        {/* Patient Video (small overlay) */}
        <div
          style={{
            position: "absolute",
            bottom: "80px",
            right: "20px",
            width: "200px",
            height: "150px",
            borderRadius: "8px",
            overflow: "hidden",
            border: "2px solid white",
          }}
        >
          <video
            autoPlay
            playsInline
            muted
            style={{ width: "100%", height: "100%", objectFit: "cover" }}
          />
          <div
            style={{
              position: "absolute",
              bottom: "5px",
              left: "5px",
              backgroundColor: "rgba(0,0,0,0.6)",
              color: "white",
              padding: "2px 6px",
              borderRadius: "4px",
              fontSize: "12px",
            }}
          >
            You
          </div>
        </div>

        {/* Controls */}
        <Group position="center" spacing="lg" mt="md">
          <Button
            radius="xl"
            size="lg"
            color={micOn ? "#1e40af" : "gray"}
            onClick={() => setMicOn(!micOn)}
          >
            {micOn ? <Mic size={20} /> : <MicOff size={20} />}
          </Button>

          <Button
            radius="xl"
            size="lg"
            color={videoOn ? "#1e40af" : "gray"}
            onClick={() => setVideoOn(!videoOn)}
          >
            {videoOn ? <Video size={20} /> : <VideoOff size={20} />}
          </Button>

          <Button radius="xl" size="lg" color="red">
            <PhoneOff size={20} />
          </Button>
        </Group>
      </Paper>
    </div>
  );
}
