#!/bin/bash

/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 --admin-only -c standalone-full.xml &

until curl -s http://localhost:9990/management/ > /dev/null; do
  sleep 1
done

/opt/jboss/wildfly/bin/jboss-cli.sh --connect --file=/app/create-queue.cli

/opt/jboss/wildfly/bin/jboss-cli.sh --connect command=:shutdown

exec /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 -c standalone-full.xml
