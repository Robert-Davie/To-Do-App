import kotlinx.serialization.Serializable
import java.util.*


@Serializable
data class Task (
    val iD: Int,
    var name: String,
    var description: String?,
    var priority: Int,
    var complete: Boolean,
    var dueDate: String?,
)