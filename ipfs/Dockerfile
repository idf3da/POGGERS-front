FROM alpine:latest
RUN apk add go-ipfs

RUN mkdir /ipfs
ADD ./node /ipfs
WORKDIR /ipfs

EXPOSE 9096 9094 9095 5001

ENV CLUSTER_SECRET=45fde172f068699b7aecc5b86d7d150f6694aad1b5559360eab17caa0ff4a0b8
RUN ./ipfs-cluster-service init

RUN ipfs init

RUN ipfs daemon &

CMD ["./ipfs-cluster-service", "daemon", "--bootstrap", "/ip4/172.17.0.1/tcp/9096", "&", "ipfs", "daemon"]