#!/bin/bash

CLIENT=client/target
SERVER=server/target

mvn clean install
tar xzf $SERVER/mapReduce-server-1.0-SNAPSHOT-bin.tar.gz -C $SERVER
tar xzf $CLIENT/mapReduce-client-1.0-SNAPSHOT-bin.tar.gz -C $CLIENT
cd $SERVER/mapReduce-server-1.0-SNAPSHOT
find . -name "*.sh" -exec chmod +x {} \;
x-terminal-emulator -e ./run-server.sh
cd ../../../
cd $CLIENT/mapReduce-client-1.0-SNAPSHOT
find . -name "*.sh" -exec chmod +x {} \;
x-terminal-emulator