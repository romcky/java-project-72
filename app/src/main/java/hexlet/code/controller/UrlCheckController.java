package hexlet.code.controller;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class UrlCheckController {
    public static void create(Context context) {
        Long urlId = context.pathParamAsClass("id", Long.class).get();
        try {
            Url url = UrlRepository.findById(urlId).orElseThrow(NotFoundResponse::new);
            Connection connection = Jsoup.connect(url.getName());
            Document document = connection.get();
            int statusCode = connection.response().statusCode();
            String title = document.title();
            Element h1Element = document.selectFirst("h1");
            String h1 = (h1Element == null) ? "" : h1Element.text();
            Element descriptionElement = document.selectFirst("meta[name=description]");
            String description = (descriptionElement == null) ? "" : descriptionElement.attr("content");
            UrlCheckRepository.save(UrlCheck.builder()
                    .statusCode(statusCode)
                    .title(title)
                    .h1(h1)
                    .description(description)
                    .createdAt(LocalDateTime.now())
                    .urlId(urlId)
                    .build());
        } catch (SQLException e) {
            context.sessionAttribute("flash", "Ошибка в работе СУБД: " + e.getMessage());
        } catch (Exception e) {
            context.sessionAttribute("flash", "Ссылка не работает: " + e.getMessage());
        } finally {
            context.redirect(NamedRoutes.urlPath(urlId));
        }
    }


    /*
        public static void create(Context context) {
        String link = context.formParamAsClass("url", String.class).get().toLowerCase().trim();
        context.sessionAttribute("link", link);
        try {
            URL linkUrl = new URI(link).toURL();
            link = linkUrl.getProtocol() + "://" + linkUrl.getHost()
                    + (linkUrl.getPort() != -1 ? ":" + linkUrl.getPort() : "");
            if (UrlRepository.findByName(link).isPresent()) {
                context.sessionAttribute("flash", "Ссылка уже содержится");
            } else {
                UrlRepository.save(new Url(link));
                context.sessionAttribute("flash", "Ссылка успешно добавлена");
                context.consumeSessionAttribute("link");
            }
        } catch (SQLException e) {
            context.sessionAttribute("flash", "Ошибка в работе СУБД");
        } catch (Exception e) {
            context.sessionAttribute("flash", "Неверная ссылка");
        } finally {
            context.redirect(NamedRoutes.rootPath());
        }
    }


        public static void show(Context context) {
        UrlPage page = new UrlPage();
        try {
            Long id = context.pathParamAsClass("id", Long.class).get();
            page.setUrl(UrlRepository.findById(id).orElseThrow(Exception::new));
        } catch (SQLException e) {
            page.setFlash("Ошибка в работе СУБД");
        } catch (Exception e) {
            page.setFlash("Указан несуществующий id");
        } finally {
            context.render("urls/show.jte", model("page", page));
        }
    }
     */
}
