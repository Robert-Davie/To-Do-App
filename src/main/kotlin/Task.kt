import java.util.*

data class Task (
    val iD: Int,
    val name: String,
    val description: String?,
    val priority: Int,
    var complete: Boolean,
    val dueDate: Date?,
)