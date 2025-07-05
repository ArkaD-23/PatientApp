package main

import (
	"fmt"
	"log"
	"net/http"

	"video_chat_service/internal/handler"
)

func main() {

	http.HandleFunc("/ws", handler.WebSocketHandler)

	fmt.Println("Signalling server started at :8080")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatalf("server failed: %v", err)
	}
}
