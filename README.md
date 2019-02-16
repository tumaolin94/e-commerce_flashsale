# e-commerce_flashsale


Optimization result:

Test by Jmeter with 5000 * 10 threads

4 Cores, 16 GB Memory per server

1. original QPS: 1310

2. Using redis to catch hot object: 1526

3. Seperate frontend and backend, only transfer result instead of rendering HTML: 1933

4. Using RabbitMQ, ordering async: 2884

5. Moving redis, MySQL and RabbitMQ to another server: QPS 4620
