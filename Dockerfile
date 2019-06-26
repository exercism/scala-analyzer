FROM gradle:jdk-alpine

ARG SBT_VERSION=1.2.8

USER root

# Install sbt
RUN apk add --no-cache -X http://dl-cdn.alpinelinux.org/alpine/edge/testing sbt

COPY --chown=gradle:gradle . .
RUN sbt assembly
RUN mkdir -p /opt/analyzer
RUN mkdir -p /opt/analyzer/lib

RUN cp /home/gradle/target/scala-2.12/scala-analyzer-assembly-0.1.0-SNAPSHOT.jar /opt/analyzer/lib/scala-analyzer-0.1.0-SNAPSHOT.jar
COPY bin/analyze.sh /opt/analyzer/bin/
COPY optimal-solutions /opt/analyzer/optimal-solutions

RUN chown -R gradle:gradle /opt/analyzer

WORKDIR /opt/analyzer

ENTRYPOINT ["/opt/analyzer/bin/analyze.sh"]