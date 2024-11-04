package fe

import fe.page.manager.ManagerMorePage
import fe.page.manager.ManagerPage
import fe.page.player.PlayerPage
import fe.page.presenter.PresenterMorePage
import fe.page.presenter.PresenterPage
import js.objects.jso
import mui.material.CssBaseline
import mui.material.PaletteMode
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
import react.FC
import react.Props
import react.create
import react.router.RouteObject
import react.router.dom.RouterProvider
import react.router.dom.createBrowserRouter
import remix.run.router.Router

internal val App = FC<Props> {

    ThemeProvider {
        theme = darkTheme

        CssBaseline {}

        RouterProvider {
            router = createRouter()
        }
    }
}

private val darkTheme = createTheme(
    jso {
        palette = jso {
            mode = PaletteMode.dark
        }
    }
)

private fun createRouter(): Router {

    return createBrowserRouter(
        arrayOf(
            RouteObject(
                path = "/",
                element = PlayerPage.create(),
            ),
            RouteObject(
                path = "/manager",
                element = ManagerPage.create(),
            ),
            RouteObject(
                path = "/manager-more",
                element = ManagerMorePage.create(),
            ),
            RouteObject(
                path = "/presenter",
                element = PresenterPage.create(),
            ),
            RouteObject(
                path = "/presenter-more",
                element = PresenterMorePage.create(),
            ),
        )
    )
}
