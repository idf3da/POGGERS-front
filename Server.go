package main

import (
	"fmt"
	"io"
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

	PORT := ":" + arguments[1]
	listener, err := net.Listen("tcp", PORT)
	check(err)
	defer listener.Close()

	conn, err := listener.Accept()
	check(err)

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

	fmt.Println(netData)
}
