"use client";

import { useEffect, useRef, useState } from "react";
import { Paper, Button, Group } from "@mantine/core";
import { Mic, MicOff, Video, VideoOff, PhoneOff, PhoneIcon } from "lucide-react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export default function VideoCall({ localId, remoteId }) {
  const [micOn, setMicOn] = useState(true);
  const [videoOn, setVideoOn] = useState(true);

  const localVideoRef = useRef(null);
  const remoteVideoRef = useRef(null);
  const pcRef = useRef(null);
  const stompClientRef = useRef(null);

  useEffect(() => {
    console.log("📡 Initializing PeerConnection for", localId, "->", remoteId);

    // ✅ Create Peer Connection
    pcRef.current = new RTCPeerConnection({
      iceServers: [{ urls: "stun:stun.l.google.com:19302" }],
    });

    // Remote stream
    pcRef.current.ontrack = (event) => {
      console.log("🎥 Remote track received:", event.streams);
      if (remoteVideoRef.current) {
        remoteVideoRef.current.srcObject = event.streams[0];
      }
    };

    // ICE candidates
    pcRef.current.onicecandidate = (event) => {
      if (event.candidate && stompClientRef.current) {
        console.log("❄️ Sending ICE candidate:", event.candidate);
        stompClientRef.current.publish({
          destination: "/app/webrtc.ice",
          body: JSON.stringify({
            type: "ICE",
            senderId: localId,
            recipientId: remoteId,
            data: event.candidate,
          }),
        });
      }
    };

    // ✅ Capture local camera/mic
    navigator.mediaDevices
      .getUserMedia({ video: true, audio: true })
      .then((stream) => {
        console.log("📷 Local stream captured:", stream);
        if (localVideoRef.current) localVideoRef.current.srcObject = stream;
        stream.getTracks().forEach((track) => {
          console.log("➕ Adding local track:", track.kind);
          pcRef.current?.addTrack(track, stream);
        });
      })
      .catch((err) => {
        console.error("🚨 Error accessing media devices:", err);
      });

    // ✅ Connect to STOMP
    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8082/ws-chat"),
      reconnectDelay: 5000,
    });

    client.onConnect = () => {
      console.log("🔌 STOMP connected");
      client.subscribe(`/topic/webrtc.${localId}`, (msg) => {
        const message = JSON.parse(msg.body);
        console.log("📩 Received signaling message:", message);

        if (message.type === "OFFER") {
          console.log("📨 Processing OFFER from", message.senderId);
          pcRef.current
            ?.setRemoteDescription(new RTCSessionDescription(message.data))
            .then(() => pcRef.current?.createAnswer())
            .then((answer) => {
              console.log("📤 Sending ANSWER");
              pcRef.current?.setLocalDescription(answer);
              client.publish({
                destination: "/app/webrtc.answer",
                body: JSON.stringify({
                  type: "ANSWER",
                  senderId: localId,
                  recipientId: message.senderId,
                  data: answer,
                }),
              });
            });
        } else if (message.type === "ANSWER") {
          console.log("📨 Processing ANSWER");
          pcRef.current?.setRemoteDescription(
            new RTCSessionDescription(message.data)
          );
        } else if (message.type === "ICE") {
          console.log("📨 Adding remote ICE candidate:", message.data);
          pcRef.current?.addIceCandidate(new RTCIceCandidate(message.data));
        }
      });
    };

    client.onStompError = (frame) => {
      console.error("🚨 STOMP error:", frame.headers["message"], frame.body);
    };

    client.activate();
    stompClientRef.current = client;

    return () => {
      console.log("🛑 Cleaning up peer connection and STOMP");
      client.deactivate();
      pcRef.current?.close();
    };
  }, [localId, remoteId]);

  const startCall = async () => {
    if (!pcRef.current || !stompClientRef.current) {
      console.warn("⚠️ Cannot start call, connection not ready");
      return;
    }
    console.log("📞 Creating OFFER...");
    const offer = await pcRef.current.createOffer();
    await pcRef.current.setLocalDescription(offer);

    console.log("📤 Sending OFFER to", remoteId, offer);
    stompClientRef.current.publish({
      destination: "/app/webrtc.offer",
      body: JSON.stringify({
        type: "OFFER",
        senderId: localId,
        recipientId: remoteId,
        data: offer,
      }),
    });
  };

  return (
    <div
      style={{
        backgroundColor: "#f9fafb",
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        margin: "0 auto",
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
        {/* Remote Video */}
        <div
          style={{
            flex: 1,
            backgroundColor: "#000",
            borderRadius: "8px",
            position: "relative",
            height: "80%",
          }}
        >
          <video
            ref={remoteVideoRef}
            autoPlay
            playsInline
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

        {/* Local Video */}
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
            ref={localVideoRef}
            autoPlay
            playsInline
            muted
            style={{ width: "100%", height: "100%", objectFit: "cover", overflow: "hidden"}}
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
            onClick={() => {
              setMicOn(!micOn);
              const stream = localVideoRef.current?.srcObject;
              stream
                ?.getAudioTracks()
                .forEach((track) => (track.enabled = !track.enabled));
              console.log("🎙️ Mic toggled:", !micOn);
            }}
          >
            {micOn ? <Mic size={20} /> : <MicOff size={20} />}
          </Button>

          <Button
            radius="xl"
            size="lg"
            color={videoOn ? "#1e40af" : "gray"}
            onClick={() => {
              setVideoOn(!videoOn);
              const stream = localVideoRef.current?.srcObject;
              stream
                ?.getVideoTracks()
                .forEach((track) => (track.enabled = !track.enabled));
              console.log("📹 Video toggled:", !videoOn);
            }}
          >
            {videoOn ? <Video size={20} /> : <VideoOff size={20} />}
          </Button>

          <Button radius="xl" size="lg" color="green" onClick={startCall}>
            <PhoneIcon size={20} />
          </Button>
        </Group>
      </Paper>
    </div>
  );
}
