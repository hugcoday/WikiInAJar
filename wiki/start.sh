#!/bin/bash

cd ..

nohup java -jar wiki.in.a.jar 3003 &

echo "Wiki server started"
echo ""
echo "URL: http://localhost:3003/wiki"
echo ""
echo "Hit enter to continue"

read dummy
