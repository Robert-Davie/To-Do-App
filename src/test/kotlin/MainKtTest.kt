import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class MainKtTest {
    @Test
    fun `when attempting to add task, given valid user input, add task to list`(){
        val input = """
            go to work
            remember suitcase
            1
            n
        """.trimIndent()
        val inputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(inputStream)
        val data = ListData()
        addTask(data)
        System.setIn(System.`in`)
        assertTrue { data.tasks == mutableListOf(
            Task(
                1,
                "go to work",
                "remember suitcase",
                1,
                false,
                null
            )
        ) }
    }
}