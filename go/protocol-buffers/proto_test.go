package main

import (
	"fmt"
	"github.com/golang/protobuf/proto"
	"io/ioutil"
	pb "protocol-buffers/proto/pb"
	"testing"
)

func TestWriteFile(t *testing.T) {
	p0 := &pb.Person{Id: 1234,
		Name: "John Doe",
		//Email: "jdoe@example.com",
		Phones: []*pb.Person_PhoneNumber{
			{Number: "555-4321", Type: pb.Person_HOME},
		}}

	book := &pb.AddressBook{People: []*pb.Person{p0}}

	out, _ := proto.Marshal(book)
	_ = ioutil.WriteFile("book", out, 0644)
}

func TestReadFile(t *testing.T) {
	// Read the existing address book.
	in, _ := ioutil.ReadFile("book")

	bookFromFile := &pb.AddressBook{}
	_ = proto.Unmarshal(in, bookFromFile)

	fmt.Println(bookFromFile)
}
