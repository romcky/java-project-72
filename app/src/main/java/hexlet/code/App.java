package hexlet.code;

import io.javalin.Javalin;
//import io.javalin.rendering.template.JavalinJte;

import java.io.IOException;
import java.sql.SQLException;


public class App {
    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static Javalin getApp() throws IOException, SQLException {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            //config.fileRenderer(new JavalinJte());
        });
        app.get("/", ctx -> ctx.result("Hello, Wold!"));
        return app;
    }
}
