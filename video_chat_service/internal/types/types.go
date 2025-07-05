package types

type Client interface {
	SendMessage([]byte)
	GetSendChannel() chan []byte
}
