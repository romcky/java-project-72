@import hexlet.code.model.Url
@import hexlet.code.repository.UrlCheckRepository
@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.UrlsPage

@param UrlsPage page

@template.layout.page(
    page = page,
    content = @`

    <div>
        @if (page.getUrls() != null)
            <h2>Список добавленных сайтов:</h2>
            <table class="table">
                <thead>
                <th>ID</th>
                <th>Ссылка</th>
                <th>Добавлена</th>
                <th>Последняя проверка</th>
                </thead>
                <tbody>
                @for (Url url : page.getUrls())
                    <tr>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getId()}</a></td>
                        <td>${url.getName()}</td>
                        <td>${url.getCreatedAt().toString()}</td>
                        <td>
                            @if (page.getLastChecks().get(url.getId()) == null)
                                Проверок не было
                            @else
                                ${page.getLastChecks().get(url.getId()).toString()}
                            @endif


                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif


    </div>


    `
)
