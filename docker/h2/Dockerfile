# Base Java image
FROM openjdk:8-jre-slim
# From https://github.com/nemerosa/h2

# Maintainer
MAINTAINER Damien Coraboeuf <damien.coraboeuf@nemerosa.com>

# Port to expose
EXPOSE 8082
EXPOSE 9092

# Create DB
RUN mkdir -p /usr/lib/h2/test && \
    mkdir -p /usr/lib/h2/dbjmonitoring && \
    mkdir -p /usr/lib/h2/gwtjmonitoring

# Volume containing the H2 data
VOLUME /usr/lib/h2

# H2 version
ENV H2_VERSION "1.4.197"

# Download
ADD "http://repo1.maven.org/maven2/com/h2database/h2/${H2_VERSION}/h2-${H2_VERSION}.jar" /var/lib/h2/h2.jar

# Startup script
COPY h2.sh /var/lib/h2/

# Rights
RUN chmod u+x /var/lib/h2/h2.sh

# Java options
ENV JAVA_OPTIONS ""

# Additional H2 options, IFEXISTS=TRUE
ENV H2_OPTIONS ""

# Entry point
ENTRYPOINT ["/var/lib/h2/h2.sh"]