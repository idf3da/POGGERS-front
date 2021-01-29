package main

import (
	"fmt"
	"io"
	"log"
	"net"
	"os"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func main() {
	arguments := os.Args
	if len(arguments) == 1 {
		fmt.Println("Please provide port number")
		return
	}

	log.Println("Starting...")

	PORT := ":" + arguments[1]

	for i := 0; i < 10; i++ {
		listener, err := net.Listen("tcp", PORT)
		check(err)
		conn, err := listener.Accept()
		check(err)
		log.Println("New connection:", i+1)
		netData := make([]byte, 0, 4096) // big buffer
		tmp := make([]byte, 256)         // using small tmo buffer for demonstrating
		for {
			n, err := conn.Read(tmp)
			if err != nil {
				if err != io.EOF {
					fmt.Println("read error:", err)
				}
				break
			}
			netData = append(netData, tmp[:n]...)
		}
		fmt.Println("Received data:", i+1, string(netData))

		listener.Close()
		log.Println("Connection closed:", i+1)
	}
}
