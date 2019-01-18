### About
 This is a reactive implementation of a RESTful calculator service, that supports addition, subtraction, multiplication, 
 division as well as exponentiation of arbitrary-precision signed decimal numbers. It also supports result caching, so 
 if there is more than one call for the same operation on the same numbers, the result is returned from the cache 
 rather than being recomputed.
 
### Endpoints
 To add, subtract, multiply, divide numbers or raise them to some power, issue an HTTP `GET` call to a corresponding 
 endpoint (note that for the operations of addition, subtraction, and multiplication, the calculator endpoints support 
 an input of up to three numbers in the same call):
 
 `/add/{a}/{b}/{c}`
 
 `/subtract/{a}/{b}/{c}`
 
 `/multiply/{a}/{b}/{c}`
 
 `/divide/{a}/{b}`
 
 `/pow/{a}/{b}`
 
 The result is a `JSON` object, containing either a `result` field with the calculated result or an `errors` array in case
  there are any errors.
 The second parameter (the exponent) for the `pow` endpoint should be a valid integer number from the `[0,999999999]` range, all
 other parameters may be arbitrary-precision signed decimal numbers written either in decimal or exponential notation.
 `0` to the `0`th power equals `1`, division of any non-zero number by `0` returns an error and the operation of dividing
 `0` by `0` is undefined.
 If a division renders a non-terminating decimal expansion/no exact representable decimal result, an error is returned.
 
### Running
 Download [Apache Maven](https://maven.apache.org/download.cgi) and follow the [installation instructions](https://maven.apache.org/install.html).
 After that, type 
 ```bash
mvn spring-boot:run
```
 in a terminal or in a command prompt. The server will bind to [localhost:8080](http://localhost:8080) by default.