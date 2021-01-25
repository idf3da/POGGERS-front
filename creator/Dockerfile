FROM alpine
WORKDIR /creator
RUN apk add --no-cache git go
EXPOSE 8080
CMD ["go", "run", "/creator/Creator.go", "172.19.0.2" "test"]