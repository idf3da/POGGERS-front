package main

import (
	"bytes"
	"fmt"
	"image"
	"io"
	"net"
	"os"

	"github.com/qeesung/image2ascii/convert"
)

func main() {

	convertOptions := convert.DefaultOptions
	convertOptions.FixedWidth = 100
	convertOptions.FixedHeight = 130

	arguments := os.Args
	if len(arguments) == 1 {
		fmt.Println("Please provide port number")
		return
	}

	PORT := ":" + arguments[1]
	l, err := net.Listen("tcp", PORT)
	if err != nil {
		fmt.Println(err)
		return
	}
	defer l.Close()

	c, err := l.Accept()
	if err != nil {
		fmt.Println(err)
		return
	}

	netData := make([]byte, 0, 4096) // big buffer
	tmp := make([]byte, 256)         // using small tmo buffer for demonstrating
	for {
		n, err := c.Read(tmp)
		if err != nil {
			if err != io.EOF {
				fmt.Println("read error:", err)
			}
			break
		}
		//fmt.Println("got", n, "bytes.")
		netData = append(netData, tmp[:n]...)
	}

	fmt.Println("-> ", netData)

	img, _, _ := image.Decode(bytes.NewReader(netData))
	converter := convert.NewImageConverter()
	fmt.Print(converter.Image2ASCIIString(img, &convertOptions))

}
