#!/bin/bash
# Humifortis Keycloak Connector Build Script

set -e

echo "======================================"
echo "Humifortis Keycloak Connector Builder"
echo "======================================"
echo ""

# Check Java version
echo "Checking Java version..."
java -version 2>&1 | grep -E "version \"(17|18|19|20|21)" > /dev/null
if [ $? -ne 0 ]; then
    echo "❌ ERROR: Java 17 or later is required"
    echo "Current Java version:"
    java -version
    exit 1
fi
echo "✓ Java version is compatible"
echo ""

# Check Maven
echo "Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo "❌ ERROR: Maven is not installed"
    echo "Please install Maven 3.8+ and try again"
    exit 1
fi
echo "✓ Maven is installed"
echo ""

# Clean and build
echo "Building project..."
mvn clean package

if [ $? -eq 0 ]; then
    echo ""
    echo "======================================"
    echo "✓ Build successful!"
    echo "======================================"
    echo ""
    echo "JAR file location:"
    echo "  $(pwd)/target/humifortis-keycloak-connector.jar"
    echo ""
    echo "Next steps:"
    echo "  1. Copy JAR to Keycloak:"
    echo "     cp target/humifortis-keycloak-connector.jar /opt/keycloak/keycloak-26.1.4/providers/"
    echo ""
    echo "  2. Set environment variable:"
    echo "     export HUMIFORTIS_API_KEY=your_api_key_here"
    echo ""
    echo "  3. Build and restart Keycloak:"
    echo "    sudo JAVA_OPTS=\"-Dnet.bytebuddy.experimental=true -Dquarkus.hibernate-orm.persistence-xml.ignore=true\" /opt/keycloak/keycloak-26.1.4/bin/kc.sh build"
    echo "     /opt/keycloak/keycloak-26.1.4/bin/kc.sh start"
    echo ""
else
    echo ""
    echo "❌ Build failed!"
    echo "Please check the error messages above"
    exit 1
fi
