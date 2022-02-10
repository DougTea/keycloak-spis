package info.westwell.spiking.filebrowser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface FileBrowserService {

    FileBrowserUser getUserByName(String username);

    void createUser(String username);

    void login();

}

class HttpFileBrowserService implements FileBrowserService {

    static final String WELLSPIKING_TRAINING = "wellspiking-training/";
    private static final String X_AUTH = "x-auth";
    private static final String X_AUTH_REQUEST_PREFERRED_USERNAME = "X-Auth-Request-Preferred-Username";
    private static final String USER_PATH = "/api/users";
    String fileBrowserUrl;
    HttpClient client;
    private String token;

    public HttpFileBrowserService(String fileBrowserUrl) {
        this.fileBrowserUrl = fileBrowserUrl;
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public FileBrowserUser getUserByName(String username) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fileBrowserUrl.concat(USER_PATH))).GET()
                .header(X_AUTH, token).build();
        HttpResponse<InputStream> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to send get users request", e);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        FileBrowserUser[] result;
        try {
            result = objectMapper.readValue(resp.body(), FileBrowserUser[].class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse json", e);
        }
        if (result != null) {
            for (int i = 0; i < result.length; i++) {
                if (result[i].username.equals(username)) {
                    return result[i];
                }
            }
        }
        return null;
    }

    @Override
    public void createUser(String username) {
        CreateUserPlayload playload = new CreateUserPlayload(username);
        playload.getData().setScope(WELLSPIKING_TRAINING.concat(username));
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyStr;
        try {
            bodyStr = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(playload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize create user playload", e);
        }
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fileBrowserUrl.concat(USER_PATH)))
                .header(X_AUTH, token).POST(HttpRequest.BodyPublishers.ofString(bodyStr)).build();
        HttpResponse<String> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to send create user request", e);
        }
        if (resp.statusCode() != 201) {
            throw new RuntimeException("Create user response code is not 201");
        }
    }

    @Override
    public void login() {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fileBrowserUrl.concat("/api/login")))
                .POST(HttpRequest.BodyPublishers.ofString("")).header(X_AUTH_REQUEST_PREFERRED_USERNAME, "admin")
                .build();
        HttpResponse<String> resp;
        try {
            resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to send login request", e);
        }
        if (resp.statusCode() != 200) {
            throw new RuntimeException("Login request code is not 200");
        }
        token = resp.body();
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class CreateUserPlayload {
    String what = "user";
    String[] which = new String[] {};
    FileBrowserUser data = new FileBrowserUser();

    public CreateUserPlayload(String username) {
        this.data.setUsername(username);
        this.data.setPassword("wordpass");
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String[] getWhich() {
        return which;
    }

    public void setWhich(String[] which) {
        this.which = which;
    }

    public FileBrowserUser getData() {
        return data;
    }

    public void setData(FileBrowserUser data) {
        this.data = data;
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class FileBrowserUser {
    long id;
    String username;
    String password;
    String scope;
    String locale = "zh-cn";
    boolean lockPassword;
    String viewMode;
    boolean singleClick;
    Permissions perm = new Permissions();
    String[] commands;
    Sorting sorting;
    Rule[] rules;
    boolean HideDotfiles;
    boolean dateFormat;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isLockPassword() {
        return lockPassword;
    }

    public void setLockPassword(boolean lockPassword) {
        this.lockPassword = lockPassword;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public boolean isSingleClick() {
        return singleClick;
    }

    public void setSingleClick(boolean singleClick) {
        this.singleClick = singleClick;
    }

    public Permissions getPerm() {
        return perm;
    }

    public void setPerm(Permissions perm) {
        this.perm = perm;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public void setSorting(Sorting sorting) {
        this.sorting = sorting;
    }

    public Rule[] getRules() {
        return rules;
    }

    public void setRules(Rule[] rules) {
        this.rules = rules;
    }

    public boolean isHideDotfiles() {
        return HideDotfiles;
    }

    public void setHideDotfiles(boolean hideDotfiles) {
        HideDotfiles = hideDotfiles;
    }

    public boolean isDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(boolean dateFormat) {
        this.dateFormat = dateFormat;
    }

}

class Permissions {
    boolean admin;
    boolean execute = true;
    boolean create = true;
    boolean rename = true;
    boolean modify = true;
    boolean delete = true;
    boolean share = true;
    boolean download = true;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isExecute() {
        return execute;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isRename() {
        return rename;
    }

    public void setRename(boolean rename) {
        this.rename = rename;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

}

class Sorting {
    String by = "name";
    boolean asc;

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}

class Rule {
    boolean regex;
    boolean allow;
    String path;
    Regexp regexp;

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Regexp getRegexp() {
        return regexp;
    }

    public void setRegexp(Regexp regexp) {
        this.regexp = regexp;
    }
}

class Regexp {
    String raw;
    Regexp regexp;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public Regexp getRegexp() {
        return regexp;
    }

    public void setRegexp(Regexp regexp) {
        this.regexp = regexp;
    }
}
