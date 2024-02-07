package info.westwell.spiking.accesskey.credential;

import java.io.IOException;

import org.keycloak.common.util.Time;
import org.keycloak.credential.CredentialModel;
import org.keycloak.util.JsonSerialization;

public class AccessKeyCredentialModel extends CredentialModel{

    public static final String TYPE = "ACCESS_KEY";

    private final AccessKey accessKey;

    private AccessKeyCredentialModel(AccessKey accessKey) {
        this.accessKey = accessKey;
        this.setType(TYPE);
        this.setCreatedDate(Time.currentTimeMillis());
    }

    public AccessKey getAccessKey() {
        return accessKey;
    }

    public static AccessKeyCredentialModel createFromAccessKey(AccessKey accessKey){
        return new AccessKeyCredentialModel(accessKey);
    }

    public static AccessKeyCredentialModel createFromCredentialModel(CredentialModel model) {
        try {
            AccessKey credentialData = JsonSerialization.readValue(model.getSecretData(), AccessKey.class);

            AccessKeyCredentialModel accessKeyCredentialModel = new AccessKeyCredentialModel(credentialData);
            accessKeyCredentialModel.setUserLabel(model.getUserLabel());
            accessKeyCredentialModel.setCreatedDate(model.getCreatedDate());
            accessKeyCredentialModel.setType(AccessKeyCredentialModel.TYPE);
            accessKeyCredentialModel.setId(model.getId());
            accessKeyCredentialModel.setSecretData(model.getSecretData());
            accessKeyCredentialModel.setCredentialData(model.getCredentialData());
            return accessKeyCredentialModel;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
