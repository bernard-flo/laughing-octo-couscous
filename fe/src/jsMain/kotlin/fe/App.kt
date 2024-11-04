package fe

import fe.page.manager.ManagerMorePage
import fe.page.manager.ManagerPage
import fe.page.player.PlayerPage
import fe.page.presenter.PresenterMorePage
import fe.page.presenter.PresenterPage
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
}
