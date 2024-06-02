package main

import {
	"fmt",
	"net/http"
}

func main() {
	http.handler(",", func(w http.responseWriter, r *http.Request) {
		fmt.Fprintf(w, "GO Scratch World!")
	})

	fmt.Println("Server is running on Port 8080")
	http.ListenAndServe(":8080", nil)
}