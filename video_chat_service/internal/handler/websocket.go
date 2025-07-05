package handler

import (
	"net/http"

	"github.com/gorilla/websocket"
	"video_chat_service/internal/client"
)

var upgrader = websocket.Upgrader{

	CheckOrigin: func(r *http.Request) bool { return true },
}

func WebSocketHandler(w http.ResponseWriter, r *http.Request) {

	roomID := r.URL.Query().Get("room")

	if roomID == "" {
		http.Error(w, "room required", http.StatusBadRequest)
		return
	}

	conn, err := upgrader.Upgrade(w, r, nil)

	if err != nil {
		http.Error(w, "could not upgrade to websocket", http.StatusInternalServerError)
		return
	}

	client.NewClient(conn, roomID)
}
