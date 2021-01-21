package main

import (
	"bufio"
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

func main() {

	arguments := os.Args
	if len(arguments) == 1 {
		fmt.Println("Please provide host:port.")
		return
	}

	reader := bufio.NewReader(os.Stdin)
	fmt.Print(">> ")
	text, _ := reader.ReadString('\n')
	text = text[:len(text)-1]

	dat, err := ioutil.ReadFile(text)
	check(err)

	CONNECT := arguments[1]
	c, err := net.Dial("tcp", CONNECT)
	if err != nil {
		fmt.Println(err)
		return
	}

	_, err = c.Write(dat)
	fmt.Println(len(dat))
	check(err)

	err = c.Close()
	check(err)

}
