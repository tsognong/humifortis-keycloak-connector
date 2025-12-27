# Changelog

All notable changes to the Humifortis Keycloak Connector will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-12-27

### Added
- Initial release of Humifortis Keycloak Connector
- Event listener SPI implementation for capturing authentication events
- RBA authenticator SPI implementation for risk-based access control
- Support for Keycloak 22.x, 23.x, and 24.x
- Comprehensive event monitoring for security-relevant events:
  - Login success/failure
  - Logout events
  - User registration
  - Password updates and resets
  - Email updates and verification
  - Token operations (refresh, revoke, introspect)
  - MFA (TOTP) changes
  - Account deletion
  - Admin operations
- Risk-based authentication with three actions:
  - ALLOW: Grant access immediately (risk 0-59)
  - CHALLENGE_MFA: Require additional authentication (risk 60-79)
  - BLOCK: Deny access with custom message (risk 80-100)
- Centralized configuration via environment variables
- Async event sending to minimize performance impact
- Fallback behavior when SaaS is unreachable
- Cross-system entity ID format for unified tracking
- Comprehensive documentation:
  - README with quick start guide
  - INSTALLATION guide with detailed setup instructions
  - API documentation
- Apache 2.0 license
- GitHub Actions CI/CD pipeline

### Architecture
- Thin client design (< 500 lines of core logic)
- No local policy engine or risk calculation
- All intelligence centralized in Humifortis SaaS
- RESTful API communication with SaaS platform
- Event-driven architecture for real-time monitoring

### Configuration
- `HUMIFORTIS_API_URL`: SaaS API endpoint
- `HUMIFORTIS_API_KEY`: API key for authentication
- `HUMIFORTIS_TIMEOUT_MS`: HTTP request timeout
- `HUMIFORTIS_FALLBACK_ALLOW`: Behavior when SaaS unreachable

### Technical Details
- Java 17+ compatibility
- Maven build system
- Keycloak SPI integration
- Gson for JSON processing
- Java 11+ HttpClient for HTTP communication
- JBoss Logging for diagnostics

## [Unreleased]

### Planned Features
- Enhanced logging and diagnostics
- Metrics export (Prometheus format)
- Health check endpoint
- Configuration validation
- Additional event types
- Performance optimizations
- Circuit breaker for SaaS communication
- Request retry logic
- Event batching for high-volume scenarios

[1.0.0]: https://github.com/humifortis/keycloak-connector/releases/tag/v1.0.0
