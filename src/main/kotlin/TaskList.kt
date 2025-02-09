class TaskList {
    var title = ""
    var owner = ""
    var currentIDCount = 0
    var tasks = mutableListOf<Task>()
    fun addDefaultTasks() {
        tasks.add(Task(
            1,
            "go to gym",
            null,
            1,
            false,
            null
        ))
        tasks.add(Task(
            2,
            "go to shops",
            "going to Tesco",
            4,
            true,
            null
        ))
        tasks.add(Task(
            3,
            "exam tomorrow",
            "I am stressed",
            2,
            false,
            null
        ))
        currentIDCount += 3
    }

    fun addTask(taskIn: Task){
        if (taskIn.iD in tasks.map { it.iD }) {
            println("ID number ${taskIn.iD} already in use")
            return
        }
        tasks.addLast(taskIn)
        currentIDCount++
        tasks.sortBy { it.priority }
        println("item ${taskIn.name} added")
    }

    fun getTaskPositionByName(name:String): Int? {
        for (i in 0..tasks.size) {
            if (tasks[i].name == name) {
                return i
            }
        }
        return null
    }

    fun prettyPrint(showComplete: Boolean = true, showIncomplete: Boolean = true) {
        if (tasks.size > 0) {
            println("+" + "-".repeat(58) + "+")
        }
        for(task in tasks){
            if (!showComplete and task.complete) {
                continue
            }
            if (!showIncomplete and !task.complete) {
                continue
            }
            println("| " + task.name + " ".repeat(58 - 1 - task.name.length) + "|")
            println("+" + "-".repeat(58) + "+")
        }
    }
}