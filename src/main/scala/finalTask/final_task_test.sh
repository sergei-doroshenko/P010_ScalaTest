#!/usr/bin/env bash
curl -d '{"name":"Mr. Fergusson"}' -H "Content-Type: application/json" -X POST localhost:8080/teachers && echo
curl -d '{"name":"Mathimatics","teacherId":1}' -H "Content-Type: application/json" -X POST localhost:8080/courses && echo
curl -d '{"name":"Computer since","teacherId":1}' -H "Content-Type: application/json" -X POST localhost:8080/courses && echo
curl localhost:8080/courses && echo
curl -d '{"name":"Adam"}' -H "Content-Type: application/json" -X POST localhost:8080/students && echo
curl localhost:8080/students && echo
curl -d '{"courseId":1}' -H "Content-Type: application/json" -X POST localhost:8080/students/1/courses && echo
curl localhost:8080/students && echo
curl -d '{"courseId":1,"feedback":"Cool work!"}' -H "Content-Type: application/json" -X PUT localhost:8080/students/1/courses && echo
curl localhost:8080/students