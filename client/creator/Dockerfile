FROM alpine:latest
RUN apk add --no-cache go
RUN export PATH=$PATH:/usr/local/go/bin
RUN mkdir /creator
ADD . /creator
WORKDIR /creator
EXPOSE 8080
# CMD "go run Creator.go 172.17.0.2 test"
CMD ["go", "run", "Creator.go", "172.17.0.2:8080", "test"]