package main

import (
	"fmt"
	"io/ioutil"
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
	conn, err := net.Dial("tcp", CONNECT)
	if err != nil {
		fmt.Println(err)
		return
	}

	file, err := ioutil.ReadFile(arguments[2])
	check(err)

	_, err = conn.Write(file)
	check(err)

	err = conn.Close()
	if err != nil {
		panic(err)
	}
}
