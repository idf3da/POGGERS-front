package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net"
	"os"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func send_image() {}

func main() {
	arguments := os.Args
	if len(arguments) == 1 {
		fmt.Println("Please provide host:port.")
		return
	} else if len(arguments) == 2 {
		fmt.Println("And file path.")
	}
	CONNECT := arguments[1]
	log.Println("Starting...")
	for i := 0; i < 10; i++ {
		conn, err := net.Dial("tcp", CONNECT)
		if err != nil {
			fmt.Println(err)
			return
		}
		log.Println("Connection to server established:", i+1)

		file, err := ioutil.ReadFile(arguments[2])
		check(err)

		_, err = conn.Write(file)
		check(err)
		log.Println("Data sent:", i+1)

		err = conn.Close()
		check(err)
		log.Println("Connection to server closed:", i+1)
	}
}
