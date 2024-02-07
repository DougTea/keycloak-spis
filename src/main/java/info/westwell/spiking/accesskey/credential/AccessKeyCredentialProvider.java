package info.westwell.spiking.accesskey.credential;

import java.io.IOException;

import org.jboss.logging.Logger;
import org.keycloak.common.util.Time;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.credential.CredentialTypeMetadata;
import org.keycloak.credential.CredentialTypeMetadataContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.util.JsonSerialization;

public class AccessKeyCredentialProvider
        implements CredentialProvider<AccessKeyCredentialModel>, CredentialInputValidator {

    private static final Logger logger = Logger.getLogger(AccessKeyCredentialProvider.class);

    protected KeycloakSession session;

    public AccessKeyCredentialProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return getType().equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType))
            return false;
        return user.credentialManager().getStoredCredentialsByTypeStream(credentialType).findAny().isPresent();
    }

    public boolean isConfiguredFor(RealmModel realm, UserModel user) {
        return isConfiguredFor(realm, user, getType());
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!(input instanceof UserCredentialModel)) {
            logger.debug("Expected instance of UserCredentialModel for CredentialInput");
            return false;
        }
        if (!input.getType().equals(getType())) {
            return false;
        }
        String challengeResponse = input.getChallengeResponse();
        if (challengeResponse == null) {
            return false;
        }
        CredentialModel credentialModel = user.credentialManager().getStoredCredentialById(input.getCredentialId());
        AccessKeyCredentialModel akcm = getCredentialFromModel(credentialModel);
        return akcm.getAccessKey().getAccessKey().equals(challengeResponse);
    }

    @Override
    public String getType() {
        return AccessKeyCredentialModel.TYPE;
    }

    @Override
    public CredentialModel createCredential(RealmModel realm, UserModel user,
            AccessKeyCredentialModel credentialModel) {
        if (credentialModel.getCreatedDate() == null) {
            credentialModel.setCreatedDate(Time.currentTimeMillis());
        }
        return user.credentialManager().createStoredCredential(credentialModel);
    }

    @Override
    public boolean deleteCredential(RealmModel realm, UserModel user, String credentialId) {
        return user.credentialManager().removeStoredCredentialById(credentialId);
    }

    @Override
    public AccessKeyCredentialModel getCredentialFromModel(CredentialModel model) {
        return AccessKeyCredentialModel.createFromCredentialModel(model);
    }

    @Override
    public CredentialTypeMetadata getCredentialTypeMetadata(CredentialTypeMetadataContext metadataContext) {
        return CredentialTypeMetadata.builder()
                .type(getType())
                .category(CredentialTypeMetadata.Category.BASIC_AUTHENTICATION)
                .displayName(AccessKeyCredentialProviderFactory.PROVIDER_ID)
                .helpText("access-key")
                .createAction(AccessKeyCredentialProviderFactory.PROVIDER_ID)
                .removeable(true)
                .build(session);
    }
}