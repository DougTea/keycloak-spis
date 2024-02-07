package info.westwell.spiking.accesskey.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessKey {

    private final String accessKey;

    @JsonCreator
    public AccessKey(@JsonProperty("accessKey") String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

}
