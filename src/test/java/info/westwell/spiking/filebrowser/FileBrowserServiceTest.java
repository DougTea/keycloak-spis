package info.westwell.spiking.filebrowser;

import org.junit.Test;

public class FileBrowserServiceTest {

    FileBrowserService service = new HttpFileBrowserService("http://10.99.209.101");

    @Test
    public void testCreateUser() throws Exception {
        service.login();
        FileBrowserUser user = service.getUserByName("test");
        if (user == null) {
            service.createUser("test");
        }
        service.getUserByName("test");
    }

}
