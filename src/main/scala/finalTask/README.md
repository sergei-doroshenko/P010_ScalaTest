# Http REST application
### curl commands
```bash
$ curl -d {\"name\":\"Adam\"} -H "Content-Type: application/json" -X POST localhost:8080/students
```  
Result: `Student added` 
```bash
$ curl localhost:8080/students
``` 
Result: `[{"id":1,"name":"Adam"},{"id":2,"name":"Hanna"}]`
```bash
$ curl localhost:8080/students/1
```  
Result: `{"id":1,"name":"Adam"}`  
```bash
$ curl -X DELETE localhost:8080/students/1
```  
Result: `Student deleted`  
```bash
$ curl localhost:8080/students/1
```  
Result: `The requested resource could not be found.`
