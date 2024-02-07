package info.westwell.spiking.accesskey.action;

import javax.ws.rs.core.Response;

import org.keycloak.authentication.CredentialRegistrator;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.credential.CredentialProvider;

import info.westwell.spiking.accesskey.credential.AccessKey;
import info.westwell.spiking.accesskey.credential.AccessKeyCredentialModel;
import info.westwell.spiking.accesskey.credential.AccessKeyCredentialProvider;
import info.westwell.spiking.accesskey.credential.AccessKeyCredentialProviderFactory;

public class AccessKeyAction implements RequiredActionProvider, CredentialRegistrator {
    
    public static final String PROVIDER_ID="access_key_config";

    @Override
    public void close() {
    }

    @Override
    public void evaluateTriggers(RequiredActionContext context) {
    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        Response challenge = context.form().createForm("access-key-config.ftl");
        context.challenge(challenge);
    }

    @Override
    public void processAction(RequiredActionContext context) {
                String accessKey = (context.getHttpRequest().getDecodedFormParameters().getFirst("access_key"));
        AccessKeyCredentialProvider akcp = (AccessKeyCredentialProvider) context.getSession().getProvider(CredentialProvider.class, AccessKeyCredentialProviderFactory.PROVIDER_ID);
        akcp.createCredential(context.getRealm(), context.getUser(),AccessKeyCredentialModel.createFromAccessKey(new AccessKey(accessKey)));
        context.success();
    }

}
