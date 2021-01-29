FROM alpine:latest
RUN apk add --no-cache go
RUN export PATH=$PATH:/usr/local/go/bin
RUN mkdir /server
ADD . /server
WORKDIR /server
EXPOSE 8080
CMD ["go", "run", "Server.go", "8080"]