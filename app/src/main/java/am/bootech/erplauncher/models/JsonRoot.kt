package am.bootech.erplauncher.models

data class JsonRoot(
    val direction: String,
    val width: String,
    val height: String,
    val positioning: String,
    val name: String,
    val type: Type,
    val uuid: String,
    val created: String, // New property
    val subframes: List<String>,
    val widgets: List<String>,
    val padding: String, // New property
    val margin: String, // New property
    val description: String,
    val backgroundColor: String,
    val kind: String
)