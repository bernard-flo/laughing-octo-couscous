package fe

import react.FC
import react.Props
import react.create
import react.router.RouteObject
import react.router.dom.RouterProvider
import react.router.dom.createBrowserRouter

internal val App = FC<Props> {

    RouterProvider {
        router = createBrowserRouter(
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
                    path = "/presenter",
                    element = PresenterPage.create(),
                ),
            )
        )
    }
}
