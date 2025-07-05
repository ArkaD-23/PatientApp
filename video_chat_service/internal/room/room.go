package room

import (
	"sync"
	"video_chat_service/internal/types"
)

type Message struct {
	Sender types.Client
	Data   []byte
}

type Room struct {
	Clients    map[types.Client]bool
	Register   chan types.Client
	Unregister chan types.Client
	Broadcast  chan Message
}

var (
	rooms = make(map[string]*Room)
	mutex = sync.Mutex{}
)

func GetRoom(id string) *Room {
	mutex.Lock()
	defer mutex.Unlock()

	if r, ok := rooms[id]; ok {
		return r
	}

	room := &Room{
		Clients:    make(map[types.Client]bool),
		Register:   make(chan types.Client),
		Unregister: make(chan types.Client),
		Broadcast:  make(chan Message),
	}
	rooms[id] = room

	go room.run()
	return room
}

func (r *Room) run() {
	for {
		select {
		case client := <-r.Register:
			r.Clients[client] = true

		case client := <-r.Unregister:
			if _, ok := r.Clients[client]; ok {
				delete(r.Clients, client)
				close(client.GetSendChannel()) // ✅ use interface method
			}

		case message := <-r.Broadcast:
			for client := range r.Clients {
				if client != message.Sender {
					client.SendMessage(message.Data) // ✅ use interface method
				}
			}
		}
	}
}
