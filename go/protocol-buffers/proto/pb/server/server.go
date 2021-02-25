package main

import (
	"context"
	"encoding/json"
	"google.golang.org/grpc"
	"log"
	"net"
	"protocol-buffers/proto/pb"
)

type server struct {
	pb.UnimplementedFooServiceServer
}

func (s *server) GetSomething(ctx context.Context, in *pb.AddressBook) (*pb.AddressBook, error) {
	log.Println(json.Marshal(in))
	return in, nil
}

func main() {
	lis, err := net.Listen("tcp", "127.0.0.1:8080")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	pb.RegisterFooServiceServer(s, &server{})
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
