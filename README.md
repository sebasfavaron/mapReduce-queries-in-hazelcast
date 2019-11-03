# pod-tp2

### Debian based with x-terminal-emulator
```
chmod +x master-script.sh
./master-script.sh
```
Compiles the project and automatically opens two terminals
* Terminal 1: server running
* **Terminal 2: Execute the desire query, the working directory is already set**


### Unix based (the long way)

#### On root folder
```
mvn clean install
```

#### Terminal 1: run the server
```
cd <repository-root>/server/target
tar xzf mapReduce-server-1.0-SNAPSHOT-bin.tar.gz
cd mapReduce-server-1.0-SNAPSHOT
chmod +x run-server.sh
./run-server.sh [-Dport=<port number>]
```

#### Terminal 2: run the desired query
```
cd <repository-root>/client/target
tar xzf mapReduce-client-1.0-SNAPSHOT-bin.tar.gz
cd mapReduce-client-1.0-SNAPSHOT
chmod +x query1.sh query2.sh query3.sh query4.sh
```

##### In terminal 2: Query1
```
./query1.sh -Daddresses=<IP>:<port> -DinPath=<in path> -DoutPath=<out path>
```

###### Example
```
./query1.sh -Daddresses=127.0.0.1:5701 -DinPath=./ -DoutPath=./
```

##### In terminal 2: Query2
```
./query2.sh -Daddresses=<IP>:<port> -DinPath=<in path> -DoutPath=<out path> -Dn=<number>
```

###### Example
```
./query2.sh -Daddresses=127.0.0.1:5701 -DinPath=./ -DoutPath=./ -Dn=5
```

##### In terminal 2: Query3
```
./query3.sh -Daddresses=<IP>:<port> -DinPath=<in path> -DoutPath=<out path>
```

###### Example
```
./query3.sh -Daddresses=127.0.0.1:5701 -DinPath=./ -DoutPath=./
```

##### In terminal 2: Query4
```
./query4.sh -Daddresses=<IP>:<port> -DinPath=<in path> -DoutPath=<out path> -Doaci=<OACI> -Dn=<number>
```

###### Example
```
./query4.sh -Daddresses=127.0.0.1:5701 -DinPath=./ -DoutPath=./ -Doaci=SAEZ -Dn=5
```