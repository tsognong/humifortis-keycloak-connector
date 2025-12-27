package tech.humifortis.keycloak.listener;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import tech.humifortis.keycloak.client.SaasClient;
import tech.humifortis.keycloak.client.SaasConfig;
import tech.humifortis.keycloak.mapper.EventMapper;
import tech.humifortis.keycloak.model.HumifortisEvent;

import java.util.Set;

public class HumifortisEventListener implements EventListenerProvider {
    private static final Logger logger = Logger.getLogger(HumifortisEventListener.class);
    
    // Events we care about for security monitoring
    private static final Set<EventType> MONITORED_EVENTS = Set.of(
            EventType.LOGIN,
            EventType.LOGIN_ERROR,
            EventType.LOGOUT,
            EventType.REGISTER,
            EventType.UPDATE_PASSWORD,
            EventType.UPDATE_EMAIL,
            EventType.RESET_PASSWORD,
            EventType.RESET_PASSWORD_ERROR,
            EventType.CODE_TO_TOKEN_ERROR,
            EventType.REFRESH_TOKEN_ERROR,
            EventType.REMOVE_TOTP,
            EventType.UPDATE_TOTP
    );

    private final SaasClient saasClient;
    private final EventMapper eventMapper;

    public HumifortisEventListener() {
        try {
            SaasConfig config = new SaasConfig();
            this.saasClient = new SaasClient(config);
            this.eventMapper = new EventMapper();
            logger.info("Humifortis Event Listener initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize Humifortis Event Listener", e);
            throw new RuntimeException("Failed to initialize Humifortis Event Listener", e);
        }
    }

    @Override
    public void onEvent(Event event) {
        // Log all events for debugging
        logger.debugf("Event received: type=%s, realmId=%s, userId=%s, clientId=%s", 
                event.getType(), event.getRealmId(), event.getUserId(), event.getClientId());
        
        // Only process events we care about
        if (!MONITORED_EVENTS.contains(event.getType())) {
            logger.debugf("Event type %s not in monitored events, skipping", event.getType());
            return;
        }

        logger.debugf("Processing monitored event: %s", event.getType());

        try {
            // Map Keycloak event to Humifortis format
            HumifortisEvent humiEvent = eventMapper.fromKeycloakEvent(event);
            
            // Send to SaaS (async, fire-and-forget)
            saasClient.sendEventAsync(humiEvent)
                    .exceptionally(ex -> {
                        logger.warnf("Failed to send event to Humifortis SaaS: %s - %s", 
                                event.getType(), ex.getMessage());
                        return null;
                    });
            
            logger.debugf("Event queued for sending: %s for user %s", 
                    event.getType(), event.getUserId());
        } catch (Exception e) {
            logger.error("Error processing event: " + event.getType(), e);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        // We can also monitor admin events if needed
        try {
            HumifortisEvent humiEvent = eventMapper.fromKeycloakAdminEvent(adminEvent);
            
            saasClient.sendEventAsync(humiEvent)
                    .exceptionally(ex -> {
                        logger.warnf("Failed to send admin event to Humifortis SaaS: %s", 
                                ex.getMessage());
                        return null;
                    });
            
            logger.debugf("Admin event queued for sending: %s", 
                    adminEvent.getOperationType());
        } catch (Exception e) {
            logger.error("Error processing admin event", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
