#!/bin/bash

SERVER=server/target

cd $SERVER/mapReduce-server-1.0-SNAPSHOT
x-terminal-emulator -e ./run-server.sh
