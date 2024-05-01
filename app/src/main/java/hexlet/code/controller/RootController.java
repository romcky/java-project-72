package hexlet.code.controller;


import io.javalin.http.Context;
import static io.javalin.rendering.template.TemplateUtil.model;

public class RootController {
    public static void index(Context context) {
        context.render("index.jte", model("page", null));
    }
}
