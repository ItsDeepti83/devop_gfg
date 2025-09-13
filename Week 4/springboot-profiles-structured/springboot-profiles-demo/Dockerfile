# Start from Ubuntu base image
FROM ubuntu:22.04

# Set environment variables for Maven
ENV MAVEN_VERSION=3.9.9 \
    MAVEN_HOME=/opt/maven \
    PATH=${PATH}:/opt/maven/bin

# Install required packages (Java, wget, curl, unzip, etc.)
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    wget \
    curl \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Download and install Maven
RUN wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && tar -xvzf apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && mv apache-maven-${MAVEN_VERSION} /opt/maven \
    && rm apache-maven-${MAVEN_VERSION}-bin.tar.gz

# Set working directory
WORKDIR /app

# Copy project files
COPY . /app

# Default command (can be overridden in docker run)
CMD ["mvn", "-v"]
