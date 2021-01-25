FROM alpine
WORKDIR /server
RUN apk add --no-cache git go
EXPOSE 8080
CMD ["go", "run", "/server/Server.go", "8080"]