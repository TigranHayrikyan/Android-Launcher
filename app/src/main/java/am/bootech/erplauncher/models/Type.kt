package am.bootech.erplauncher.models

data class Type(
    val type: String,
    val name: String,
    val description: String,
    val id: String,
    val uuid: String,
    val properties: List<Property>,
    val collections: List<Collection>,
    val values: List<String>,
    val methods: List<Any>,
    val literal: String,
    val vcs_enabled: String,
    val discussion_enabled: String,
    val enable_notifier: String,
    val icon: String,
    val video: String
)