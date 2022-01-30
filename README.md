# What is this?

This project demonstrates a JobRunr issue with serializing generic types when
the generic type could be a UUID.

# Database Setup

```
$ psql postgres
postgres=# CREATE ROLE uuid_serialization WITH LOGIN PASSWORD '6tHtncRH@g98' CREATEDB;
postgres=# \q
$ psql postgres -U uuid_serialization
postgres=> CREATE DATABASE uuid_serialization;
postgres=> GRANT ALL PRIVILEGES ON DATABASE uuid_serialization TO uuid_serialization;
postgres=# \q
```

# Execution

```
$ ./mvnw spring-boot:run
```

Once started, open http://localhost:8080 in your browser, which will queue 3
jobs, 2 working and 1 failing. You can view the jobs in http://localhost:8081 or
connect to the database and see the job data.

From the database, you can see the serialization issue by looking for the 3 jobs.

The successful String job looks like this:
```
...{"className":"java.io.Serializable","actualClassName":"java.lang.String","object":"Hello World"}...
```

The successful UUID job looks like this:
```
...{"className":"java.util.UUID","actualClassName":"java.util.UUID","object":"c5de0943-79d9-4caa-a757-57469cf14076"}...
```

The failing UUID job looks like this:
```
...{"className":"java.io.Serializable","actualClassName":"java.lang.String","object":"ade45040-7144-4eed-bc4e-d43fde199b14"}...
```

The failing job has an error like:
```
2022-01-30 15:06:24.487  WARN 66530 --- [pool-2-thread-3] o.jobrunr.server.BackgroundJobPerformer  : Job(id=0ddd3cb3-288c-4ec1-a339-9aeb0ff51ddb, jobName='com.example.uuidserialization.model.Jobs.process(class com.example.uuidserialization.model.UuidJob,ade45040-7144-4eed-bc4e-d43fde199b14)') processing failed: An exception occurred during the performance of the job

java.lang.reflect.InvocationTargetException: null
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]
	at org.jobrunr.server.runner.AbstractBackgroundJobRunner$BackgroundJobWorker.invokeJobMethod(AbstractBackgroundJobRunner.java:64) ~[jobrunr-4.0.3.jar:na]
	at org.jobrunr.server.runner.AbstractBackgroundJobRunner$BackgroundJobWorker.run(AbstractBackgroundJobRunner.java:38) ~[jobrunr-4.0.3.jar:na]
	at org.jobrunr.server.runner.AbstractBackgroundJobRunner.run(AbstractBackgroundJobRunner.java:20) ~[jobrunr-4.0.3.jar:na]
	at org.jobrunr.server.BackgroundJobPerformer.runActualJob(BackgroundJobPerformer.java:81) ~[jobrunr-4.0.3.jar:na]
	at org.jobrunr.server.BackgroundJobPerformer.run(BackgroundJobPerformer.java:41) ~[jobrunr-4.0.3.jar:na]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:539) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264) ~[na:na]
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635) ~[na:na]
	at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]
Caused by: java.lang.ClassCastException: class java.lang.String cannot be cast to class java.util.UUID (java.lang.String and java.util.UUID are in module java.base of loader 'bootstrap')
	at com.example.uuidserialization.model.UuidJob.process(UuidJob.java:5) ~[classes/:na]
	at com.example.uuidserialization.model.Jobs.process(Jobs.java:21) ~[classes/:na]
	... 15 common frames omitted
```
