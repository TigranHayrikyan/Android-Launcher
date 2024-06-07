package am.bootech.erplauncher.models

data class Property(
    val id: String,
    val name: String,
    val description: String,
    val value_type: String,
    val mandatory: String,
    val default_value: String,
    val readonly: String,
    val visible: String,
    val visible_in_list_view: String,
    val visible_in_tree_view: String,
    val enable_internalization: String,
    val enable_version_tracking: String,
    val icon: String,
    val video: String,
    val kind: String,
    val symmetric_kind: String,
    val symmetric: String,
    val type: String
)
