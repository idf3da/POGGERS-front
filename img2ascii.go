package main

import (
	"bytes"
	"fmt"
	"image"
	"io/ioutil"

	"github.com/qeesung/image2ascii/convert"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func main() {

	convertOptions := convert.DefaultOptions
	convertOptions.FixedWidth = 130
	convertOptions.FixedHeight = 100

	dat, err := ioutil.ReadFile("image.png")
	check(err)
	fmt.Print(dat)

	img, _, _ := image.Decode(bytes.NewReader(dat))

	converter := convert.NewImageConverter()
	fmt.Print(converter.Image2ASCIIString(img, &convertOptions))

}
