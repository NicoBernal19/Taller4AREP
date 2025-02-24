FROM openjdk:21

WORKDIR /usrapp/bin

ENV PORT 6000

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency
COPY /target/classes/web /usrapp/bin/web

CMD ["java","-cp","./classes:./dependency/*","co.edu.eci.arep.webframework.WebFramework"]