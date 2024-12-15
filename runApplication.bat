@echo off

set DB_URL=<your-url>
set DB_USERNAME=<your-username>
set DB_PASSWORD=<your-password>
set JWT_SECRET=<your-secret-key>

java -jar build/libs/task-list-0.0.1-SNAPSHOT.jar
