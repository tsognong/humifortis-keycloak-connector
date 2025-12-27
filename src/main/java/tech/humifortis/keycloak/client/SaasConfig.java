package tech.humifortis.keycloak.client;

public class SaasConfig {
    private final String apiUrl;
    private final String apiKey;
    private final int timeoutMs;
    private final boolean fallbackAllow;

    public SaasConfig() {
        this.apiUrl = getEnvOrDefault("HUMIFORTIS_API_URL", "https://api.humifortis.educosmic.tech");
        this.apiKey = getEnvOrThrow("HUMIFORTIS_API_KEY");
        this.timeoutMs = Integer.parseInt(getEnvOrDefault("HUMIFORTIS_TIMEOUT_MS", "5000"));
        this.fallbackAllow = Boolean.parseBoolean(getEnvOrDefault("HUMIFORTIS_FALLBACK_ALLOW", "true"));
    }

    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null && !value.isEmpty() ? value : defaultValue;
    }

    private String getEnvOrThrow(String key) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException("Required environment variable not set: " + key);
        }
        return value;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public boolean isFallbackAllow() {
        return fallbackAllow;
    }
}
