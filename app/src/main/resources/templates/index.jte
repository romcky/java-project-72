@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.RootPage

@param RootPage page

@template.layout.page(
    page = page,
    content = @`
        <div>
            <h1>Анализатор страниц</h1>
        </div>
        <div>
            <form method="POST" action="${NamedRoutes.urlsPath()}">
                <label> Сайт для проверки:
                    <input type="text" name="url" placeholder="введите ссылку" value="${page.getLink()}" required />
                </label>
                <input type="submit" value="Добавить">
            </form>
        </div>
    `
)