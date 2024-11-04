@file:JsModule("notistack")

package fe.ext.notistack

import react.Props

external val SnackbarProvider: react.FC<SnackbarProviderProps>

external interface SnackbarProviderProps : Props {
    var anchorOrigin: AnchorOrigin
    var autoHideDuration: Int
    var maxSnack: Int
    var variant: String
}

external interface AnchorOrigin {
    var horizontal: String
    var vertical: String
}

external fun enqueueSnackbar(message: String)
