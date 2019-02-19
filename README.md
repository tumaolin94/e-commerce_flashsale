# e-commerce_flashsale

### Requirement:
1. Spring Boot
2. MySQL
3. Redis
4. RabbitMQ

### Entry

`http://hostname:8080/login/to_login`


### Before Pressure Test
```
1. run main function of UserUtil to create multiple users into MySQL
2. if has already create users, run generateTokens() to generate tokens.txt
3. import tokens.txt into jmeter
3. run: sh jmeter -n -t jmeter_script.jmx -l result.jtl
```



### Optimization result:

Test by Jmeter with 5000 * 10 threads, 

Test request: `Buy operation: POST request`



4 Cores, 16 GB Memory per server, 1 backend server in total.

1. original QPS: 1310

2. Using redis to catch hot object: 1526

3. Seperate frontend and backend, only transfer result instead of rendering HTML: 1933

4. Using RabbitMQ, ordering async: 2884

5. Moving redis, MySQL and RabbitMQ to another server: QPS 4620
