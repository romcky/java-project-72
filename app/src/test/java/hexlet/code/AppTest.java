package hexlet.code;

import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.sql.SQLException;

public class AppTest {
    Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            Assertions.assertEquals(response.code(), 200);
            Assertions.assertTrue(response.body().string().contains("Анализатор страниц"));
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            Assertions.assertEquals(response.code(), 200);
            Assertions.assertTrue(response.body().string().contains("Список добавленных сайтов"));
        });
    }

    @Test
    public void testCreateUrlWrong() {
        JavalinTest.test(app, (server, client) -> {
            var createResponse = client.post(NamedRoutes.urlsPath(), "url=testcom");
            Assertions.assertEquals(createResponse.code(), 200);
            var showResponse = client.get(NamedRoutes.urlPath(1));
            Assertions.assertEquals(showResponse.code(), 200);
            Assertions.assertFalse(showResponse.body().string().contains("testcom"));
        });
    }

    @Test
    public void testCreateUrlRith() {
        JavalinTest.test(app, (server, client) -> {
            var createResponse = client.post(NamedRoutes.urlsPath(), "url=https://test.com");
            Assertions.assertEquals(createResponse.code(), 200);
            var showResponse = client.get(NamedRoutes.urlPath(1));
            Assertions.assertEquals(showResponse.code(), 200);
            Assertions.assertTrue(showResponse.body().string().contains("https://test.com"));
        });
    }

}
