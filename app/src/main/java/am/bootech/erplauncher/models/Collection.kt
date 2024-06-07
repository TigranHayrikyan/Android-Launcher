package am.bootech.erplauncher.models

data class Collection(
    val id: String,
    val name: String,
    val element_type: String,
    val kind: String,
    val description: String,
    val symmetric_kind: String,
    val symmetric: String,
    val icon: String,
    val type: String
)