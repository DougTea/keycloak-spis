package info.westwell.spiking.filebrowser;

import org.keycloak.Config.Scope;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class FileBrowserEventListenerProviderFactory implements EventListenerProviderFactory {

    @Override
    public void close() {
        // Nothing to close
    }

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new FileBrowserEventListenerProvider(session);
    }

    @Override
    public String getId() {
        return "filebrowser_event_listener";
    }

    @Override
    public void init(Scope arg0) {
        // No need to init
    }

    @Override
    public void postInit(KeycloakSessionFactory arg0) {
        // No post init
    }

}
