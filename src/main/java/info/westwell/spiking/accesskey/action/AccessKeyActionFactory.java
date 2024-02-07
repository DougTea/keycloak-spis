package info.westwell.spiking.accesskey.action;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class AccessKeyActionFactory implements RequiredActionFactory{

    private static final AccessKeyAction accessKeyAction = new AccessKeyAction();

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return accessKeyAction;
    }

    @Override
    public void init(Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return AccessKeyAction.PROVIDER_ID;
    }

    @Override
    public String getDisplayText() {
        return "Access Key";
    }
    
}
