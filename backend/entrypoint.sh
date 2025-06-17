#!/bin/bash

/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 -c standalone-full.xml &

until curl -s http://localhost:9990/management/ > /dev/null; do
  sleep 1
done

/opt/jboss/wildfly/bin/jboss-cli.sh --file=/app/create-queue.cli

wait