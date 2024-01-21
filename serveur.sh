javac -cp "lib/*:src/commands/*:src/data/*:src/threads/*" -d bin ./src/*.java ./src/*/*.java
java -cp "bin:lib/*" src/Serveur