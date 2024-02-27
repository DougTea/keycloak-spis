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
        try {
            setSecretData(JsonSerialization.writeValueAsString(accessKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AccessKey getAccessKey() {
        return accessKey;
    }

    public static AccessKeyCredentialModel createFromAccessKey(AccessKey accessKey){
        return new AccessKeyCredentialModel(accessKey);
    }

    public static AccessKeyCredentialModel createFromCredentialModel(CredentialModel model) {
        try {
            AccessKey accessKey = JsonSerialization.readValue(model.getSecretData(), AccessKey.class);
            AccessKeyCredentialModel accessKeyCredentialModel = new AccessKeyCredentialModel(accessKey);
            accessKeyCredentialModel.setUserLabel(model.getUserLabel());
            accessKeyCredentialModel.setId(model.getId());
            return accessKeyCredentialModel;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCredentialData() {
        return "credential";
    }

}
