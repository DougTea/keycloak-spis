package info.westwell.spiking.filebrowser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        user = service.getUserByName("test");
        assertEquals(HttpFileBrowserService.WELLSPIKING_TRAINING.concat("test"), user.scope);
        assertTrue(user.perm.create);
        assertTrue(user.perm.delete);
        assertTrue(user.perm.download);
        assertTrue(user.perm.modify);
        assertTrue(user.perm.rename);
        assertTrue(user.perm.share);
        assertTrue(user.perm.execute);
        assertEquals("zh-cn", user.locale);
    }

}
