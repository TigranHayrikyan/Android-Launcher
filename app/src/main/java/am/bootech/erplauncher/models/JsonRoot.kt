package am.bootech.erplauncher.models

data class JsonRoot(
    val Direction: String,
    val Width: String,
    val Height: String,
    val Positioning: String,
    val name: String,
    val type: Type,
    val uuid: String,
    val widget: List<String>,
    val widgets: List<String>,
    val description: String,
)