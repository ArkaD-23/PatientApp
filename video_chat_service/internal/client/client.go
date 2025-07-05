package client

import (
	"log"
	"video_chat_service/internal/types"

	"github.com/gorilla/websocket"
	"video_chat_service/internal/room"
)

type Client struct {
	Conn   *websocket.Conn
	Send   chan []byte
	RoomID string
}

func (c *Client) SendMessage(data []byte) {
	c.Send <- data
}

func (c *Client) GetSendChannel() chan []byte {
	return c.Send
}

func NewClient(conn *websocket.Conn, roomID string) types.Client {
	c := &Client{
		Conn:   conn,
		Send:   make(chan []byte),
		RoomID: roomID,
	}

	r := room.GetRoom(roomID)
	r.Register <- c

	go c.readPump(r)
	go c.writePump()

	return c
}

func (c *Client) readPump(r *room.Room) {
	defer func() {
		r.Unregister <- c
		c.Conn.Close()
	}()

	for {
		_, message, err := c.Conn.ReadMessage()
		if err != nil {
			log.Println("read error:", err)
			break
		}
		r.Broadcast <- room.Message{Sender: c, Data: message}
	}
}

func (c *Client) writePump() {
	defer c.Conn.Close()

	for msg := range c.Send {
		if err := c.Conn.WriteMessage(websocket.TextMessage, msg); err != nil {
			log.Println("write error:", err)
			break
		}
	}
}
