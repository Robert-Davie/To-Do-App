import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListDataTest {
    lateinit var data: ListData

    @BeforeEach
    fun setUp(){
        data = ListData()
    }
    @Test
    fun `given valid task, when one task is added, task appears in task list`() {
        data.addTask(task1)
        assertTrue { data.tasks == mutableListOf(task1) }
    }
    @Test
    fun `given two valid tasks, when both are added, both appear in the task list in priority order`() {
        data.addTask(task1)
        data.addTask(task2)
        assertTrue { data.tasks == mutableListOf(task2, task1) }
        assertFalse { data.tasks == mutableListOf(task1, task2) }
    }
    @Test
    fun `given task with duplicate ID number, when attempting to add task, then task should not be added`(){
        data.addTask(task1)
        data.addTask(task3)
        assertTrue { data.tasks == mutableListOf(task1) }
    }
    @Test
    fun `given task called 'buy food' in position 2 of list, when finding position, then should return 2`() {
        data.addTask(task1) // 0 position
        data.addTask(task2)
        data.addTask(task4) // 2 position
        assert(data.getTaskPositionByName("buy food") == 2)
    }
    companion object {
        val task1 = Task(1, "bake cake", null, 4, false, null)
        val task2 = Task(2, "tidy up room", null, 3, false, null)
        val task3 = Task(1, "walk dog", null, 5, false, null)
        val task4 = Task(4, "buy food", null, 5, false, null)
    }
}