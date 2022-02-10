package info.westwell.spiking.filebrowser;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

public class FileBrowserEventListenerProvider implements EventListenerProvider {

    private static final Logger LOG = Logger.getLogger(FileBrowserEventListenerProvider.class);

    FileBrowserService fileBrowserService;
    KeycloakSession session;

    public FileBrowserEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.fileBrowserService = new HttpFileBrowserService("http://filebrowser.well-spiking");
    }

    @Override
    public void close() {
        // Nothing to close
    }

    @Override
    public void onEvent(Event event) {
        // No operators on event
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean arg1) {
        if (ResourceType.USER.equals(adminEvent.getResourceType())) {
            switch (adminEvent.getOperationType()) {
                case CREATE:
                    UserModel user = this.session.users().getUserById(
                            this.session.realms().getRealm(adminEvent.getRealmId()),
                            adminEvent.getResourcePath().split("/")[1]);
                    createUserInFileBrowser(user.getUsername());
                    break;
                default:
                    break;
            }
        }
    }

    private void createUserInFileBrowser(String username) {
        fileBrowserService.login();
        FileBrowserUser user = fileBrowserService.getUserByName(username);
        if (user == null) {
            fileBrowserService.createUser(username);
            LOG.infov("Success create user {} in filebrowser", username);
        } else {
            LOG.warnv("User {} exist!", username);
        }
    }

}
