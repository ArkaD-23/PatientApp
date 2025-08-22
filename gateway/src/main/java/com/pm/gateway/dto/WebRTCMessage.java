package com.pm.gateway.dto;

import lombok.Data;

@Data
public class WebRTCMessage {

    private String type;       // OFFER, ANSWER, ICE

    private String senderId;   // patient-123

    private String recipientId;// doctor-456

    private Object data;       // SDP or ICE
}
