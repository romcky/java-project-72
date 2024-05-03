@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.UrlPage

@param UrlPage page

@template.layout.page(
    page = page,
    content = @`
        <h2>Обзор</h2>
        <div>
            @if (page.getUrl() != null)
                <table class="table">
                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <td>Ссылка</td>
                        <td>${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <td>Добавлена</td>
                        <td>${page.getUrl().getCreatedAt().toString()}</td>
                    </tr>
                    </tbody>
                </table>
            @endif
        </div>
    `
)
