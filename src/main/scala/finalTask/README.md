# Http REST application
### Scripts  
For testing use:  
1. [final_task_test.bat](final_task_test.bat)
2. [final_task_test.sh](final_task_test.sh)  
### Students
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

## Courses
Insert teacher:  
```bash
$ curl -d {\"name\":\"MrFergusson\"} -H "Content-Type: application/json" -X POST localhost:8080/teachers
```  
Insert course:  
```bash
$ curl -d {\"name\":\"Mathimatics\",\"teacherId\":1} -H "Content-Type: application/json" -X POST localhost:8080/courses
```  
Fetch all course with teachers:  
```bash
$ curl localhost:8080/courses
```  
Create student:  
```bash
$ curl -d {\"name\":\"Adam\"} -H "Content-Type: application/json" -X POST localhost:8080/students
```  
Result: `Student added: Success`  
Add course to student:  
```bash
$ curl -d {\"courseId\":1} -H "Content-Type: application/json" -X POST localhost:8080/students/1/courses
```

