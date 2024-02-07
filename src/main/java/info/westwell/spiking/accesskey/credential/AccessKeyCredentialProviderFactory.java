package info.westwell.spiking.accesskey.credential;

import org.keycloak.credential.CredentialProvider;
import org.keycloak.credential.CredentialProviderFactory;
import org.keycloak.models.KeycloakSession;

public class AccessKeyCredentialProviderFactory implements CredentialProviderFactory<AccessKeyCredentialProvider>{

    public static final String PROVIDER_ID = "access-key";

    @Override
    public CredentialProvider create(KeycloakSession session) {
        return new AccessKeyCredentialProvider(session);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
    
}
