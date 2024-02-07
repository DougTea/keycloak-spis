package info.westwell.spiking.accesskey;

import java.util.LinkedList;
import java.util.List;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

public class AccessKeyAuthenticatorFactory implements AuthenticatorFactory{

    public static final String PROVIDER_ID = "access-key-authenticator";
    private static final AccessKeyAuthenticator ACCESS_KEY_AUTHENTICATOR = new AccessKeyAuthenticator();

    @Override
    public Authenticator create(KeycloakSession session) {
        return ACCESS_KEY_AUTHENTICATOR;
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
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Access Key";
    }

    @Override
    public String getReferenceCategory() {
        return "Access Key";
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Validates the access key supplied as a 'password' form parameter in direct grant request";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return new LinkedList<>();
    }
    
}
