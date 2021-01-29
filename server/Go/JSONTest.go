package main

import (
	"bytes"
	"math/rand"
	"net/http"
	"strconv"
	"sync"
	"time"
)

var wg sync.WaitGroup

func j(str string) string {
	return "{\"items\":[{\"name\":\"name" + str + "\",\"id\":" + str + "}]}"
}

func test(url *string, client *http.Client, c int) {

	rand.Seed(time.Now().UTC().UnixNano())

	str := strconv.FormatInt(int64(rand.Intn(2147483647-1)+1), 10)

	strJson := j(str)

	req, err := http.NewRequest("POST", *url+"/create-order", bytes.NewBuffer([]byte(strJson)))
	req.Header.Set("Content-Type", "application/json")

	_, err = client.Do(req)
	if err != nil {
		wg.Done()
		return
	}

	req, err = http.NewRequest("GET", *url+"/item/"+str, bytes.NewBuffer([]byte("")))
	req.Header.Set("Content-Type", "application/json")

	client = &http.Client{}
	_, err = client.Do(req)
	if err != nil {
		wg.Done()
		return
	}
	// // fmt.Println(resp.StatusCode)
	// defer resp.Body.Close()

	wg.Done()
}

func main() {
	url := "http://localhost:8080"
	client := &http.Client{}

	c := 20000000

	wg.Add(c)
	for i := 0; i < c; i++ {
		go test(&url, client, i)
	}
	wg.Wait()
}
