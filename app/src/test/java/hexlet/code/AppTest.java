package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


import java.io.IOException;
import java.sql.SQLException;

public class AppTest {
    private Javalin app;
    private static MockWebServer mockWebServer;
    private static String mockServerUrl;
    private static String mockServerBody =
            "<html><head><title>Test Title</title></head><body></body></html>";


    @BeforeAll
    public static void startMockServer() {
        mockWebServer = new MockWebServer();
        mockServerUrl = mockWebServer.url("/").toString();
        mockWebServer.enqueue(new MockResponse().setBody(mockServerBody));
    }

    @AfterAll
    public static void shutdownMockServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @AfterEach
    public final void closeBase() {
        if (BaseRepository.dataSource != null) {
            BaseRepository.dataSource.close();
        }
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
    public void testCreateUrlRight() {
        JavalinTest.test(app, (server, client) -> {
            var createResponse = client.post(NamedRoutes.urlsPath(), "url=https://test.com");
            Assertions.assertEquals(createResponse.code(), 200);
            var showResponse = client.get(NamedRoutes.urlPath(1));
            Assertions.assertEquals(showResponse.code(), 200);
            Assertions.assertTrue(showResponse.body().string().contains("https://test.com"));
        });
    }

    @Test
    public void testUrlChecks() {
        try {
            UrlRepository.save(Url.builder().name(mockServerUrl).build());
        } catch (SQLException exception) {
            throw new AssertionError();
        }
        JavalinTest.test(app, (server, client) -> {
            Url url = UrlRepository.findByName(mockServerUrl).orElseThrow(AssertionError::new);
            var addCheckResponse = client.post(NamedRoutes.urlChecksPath(url.getId()));
            Assertions.assertEquals(addCheckResponse.code(), 200);
            Assertions.assertTrue(addCheckResponse.body().string().contains("Test Title"));
        });

    }

}
