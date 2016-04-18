Notes on developing and running locally.

Use git bash with the following configurations:

Configuring Java 8
	
	export JAVA_HOME=/c/Java/jdk1_8/

Configuring Maven 3.3.3
	
	export M2=/c/Dev/tools/maven/apache-maven-3.3.3/bin
	export M2_HOME=/c/Dev/tools/maven/apache-maven-3.3.3

Running Maven need to reference the 3.3.3 mvn.exe
	
	/c/Dev/tools/maven/apache-maven-3.3.3/bin/mvn clean install
	/c/Dev/tools/maven/apache-maven-3.3.3/bin/mvn clean package

Run Java need to reference the 1.8 java.exe 
	
	/c/Java/jdk1_8/bin/java -jar target/consumer-0.0.1-SNAPSHOT.jar
	/c/Java/jdk1_8/bin/java -jar target/producer-0.0.1-SNAPSHOT.jar -api jms
	
Use Spring Tool Suite under 
	C:\Dev\tools\eclipse\sts-3.7.3\sts-bundle\sts-3.7.3.RELEASE\STS.exe
	
With the -vm param pointing to Java 1.8 in STS.ini

	-vm
	C:\Java\jdk1_8\bin\javaw.exe 
	
Using ActiveMQ 

	C:\Dev\tools\apache-activemq-5.13.2>bin\activemq start