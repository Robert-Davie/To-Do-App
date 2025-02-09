import java.io.File
import java.io.FileWriter
import java.io.IOException


object Settings {
    var colourFormat: Boolean = true
}


fun main(){
    val taskList = TaskList()
    taskList.addDefaultTasks()
    while(true) {
        colourPrintLn("To do app menu", Colour.PURPLE)
        colourPrintLn("=".repeat(60), Colour.BLUE)
        colourPrintLn("""
            type...
            1 to view active tasks
            2 to view complete tasks
            3 to mark task as complete
            4 to add a task
            5 to view a task
            6 to update a task
            7 to delete a task
            9 to change settings
            0 to QUIT [or type q]
        """.trimIndent(), Colour.GREEN)
        colourPrintLn("=".repeat(60), Colour.BLUE)
        val userInput = readlnOrNull()
        when (userInput) {
            "1" -> {
                if (taskList.tasks.none { !it.complete }) {
                    colourPrintLn("no incomplete tasks exist", Colour.RED)
                    continue
                }
                taskList.prettyPrint(showComplete = false)
            }
            "2" -> {
                if (taskList.tasks.none { it.complete }) {
                    colourPrintLn("no complete tasks exist", Colour.RED)
                    continue
                }
                taskList.prettyPrint(showIncomplete = false)
            }
            "3" -> {
                markTaskComplete(taskList)
            }
            "4" -> {
                addTask(taskList)
            }
            "5" -> {
                viewTask(taskList)
            }
            "6" -> {
                updateTask(taskList)
            }
            "7" -> {
                deleteTask(taskList)
            }
            "9" -> {
                changeSettings()
            }
            "0" -> {
                println("Application closed")
                return
            }
            "q" -> {
                println("Application closed")
                return
            }
            else -> {
                colourPrintLn("FAILED: user command $userInput unknown", Colour.RED)
            }
        }
    }
}

fun changeSettings() {
    val isColourOnMessage = if (Settings.colourFormat) {
        "ON"
    } else {
        "OFF"
    }
    println("""
        type 1 to toggle text colour (currently $isColourOnMessage)
        type q to return to main menu
    """.trimIndent())
    val input = readlnOrNull()
    when(input) {
        "1" -> {
            Settings.colourFormat = !Settings.colourFormat
            colourPrintLn("Colour format changed", Colour.BLUE)
        }
        "q" -> {
            colourPrintLn("Returning to main menu", Colour.BLUE)
            return
        }
        else -> colourPrintLn("Input not valid, returning to main menu", Colour.RED)
    }
}

enum class Colour {
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE,
}

fun deleteTask(taskList: TaskList) {
    val taskNumber = chooseTask(
        taskList,
        "Select number of which task to delete [or press q to cancel]",
    )
    if (taskNumber != null) {
        println("Type y confirm delete task ${taskList.tasks[taskNumber].name}")
        if (readlnOrNull() == "y") {
            taskList.tasks.removeAt(taskNumber)
        } else {
            colourPrintLn("Cancelling delete action", Colour.YELLOW)
        }
    }
}

fun viewTask(taskList: TaskList) {
    val taskNumber = chooseTask(
        taskList,
        "Select number of which task to view",
        )
    println("=".repeat(40))
    if (taskNumber != null) {
        val task = taskList.tasks[taskNumber]
        colourPrintLn("   task name: ${task.name}", Colour.BLUE)
        colourPrintLn("     task ID: ${task.iD}", Colour.BLUE)
        colourPrintLn(" description: ${task.description}", Colour.BLUE)
        colourPrintLn("    priority: ${task.priority}", Colour.BLUE)
        colourPrintLn("is complete?: ${task.complete}", Colour.BLUE)
        colourPrintLn("    due date: ${task.dueDate}", Colour.BLUE)
    }
}

fun markTaskComplete(taskList: TaskList) {
    val taskPosition = chooseTask(
        taskList,
        "Select number of which task to mark as complete [or press q to cancel]",
        showComplete = false
    )
    val tasks = taskList.tasks.filter { !it.complete }
    if (taskPosition != null) {
        tasks[taskPosition].complete = true
    }
}

fun updateTask(taskList: TaskList) {
    val taskPosition = chooseTask(
        taskList,
        "Select number of which of task to update [or press q to cancel]"
    )
    if (taskPosition != null) {
        updateGivenTask(taskList, taskPosition)
    }
}

fun chooseTask(taskList: TaskList, message: String, showComplete: Boolean = true): Int? {
    val tasks = if (showComplete) {
        taskList.tasks
    } else {
        taskList.tasks.filter { !it.complete } as MutableList<Task>
    }
    val listSize = tasks.size
    if (listSize == 0) {
        colourPrintLn("No tasks available to perform action on", Colour.RED)
        return null
    }
    for (i in 0..<listSize) {
        println("$i ${tasks[i].name}")
    }
    while(true){
        colourPrintLn(message, Colour.GREEN)
        val input = readlnOrNull()
        if (input == "q"){
            return null
        }
        if(input?.toIntOrNull() in 0..<listSize) {
            colourPrintLn("task '${tasks[input!!.toInt()].name}' chosen", Colour.BLUE)
            return input!!.toInt()
        } else {
            colourPrintLn("invalid input, please try again", Colour.RED)
        }
    }
}

fun updateGivenTask(taskList: TaskList, position: Int) {
    val oldTask = taskList.tasks[position]

    val currentName = oldTask.name
    println("change current name: $currentName [y/n]")
    if (readlnOrNull()=="y"){
        println("enter new name")
        taskList.tasks[position].name = readlnOrNull()!!
    }

    val currentDescription = oldTask.description
    println("change current description: ${currentDescription ?: ""} [y/n]")
    if (readlnOrNull()=="y"){
        println("enter new description")
        taskList.tasks[position].description = readlnOrNull()!!
    }

    val currentPriority = oldTask.priority
    println("change current priority: $currentPriority [y/n]")
    if (readlnOrNull()=="y") {
        val newPriority = getPriorityForTaskFromUser()
        if (newPriority == null) {
            colourPrintLn("priority invalid, returning to main menu", Colour.RED)
            return
        }
        taskList.tasks[position].priority = newPriority
    }

    val currentComplete = oldTask.complete
    println("change current completeness: $currentComplete?[y/n]")
    if (readlnOrNull()=="y") {
        taskList.tasks[position].complete = !currentComplete
    }

    println("change date not yet supported")
}

fun addTask(dataIn: TaskList) {
    println("new task name")
    val newName = readlnOrNull()
    if (newName == "" || newName == null) {
        colourPrintLn("name cannot be empty, returning to menu", Colour.RED)
        return
    }
    println("new task description [optional]")
    val newDescription = readlnOrNull()
    val newPriority = getPriorityForTaskFromUser() ?: return
    println("add due date [y/n]")
    if (readlnOrNull() == "y") {
        println("adding dates not yet implemented")
    }
    val task = Task(
        iD = dataIn.currentIDCount + 1,
        name = newName,
        description = newDescription,
        priority = newPriority,
        complete = false,
        dueDate = null
    )
    dataIn.addTask(task)
}


fun getPriorityForTaskFromUser(): Int?{
    println("task priority, pick number between 1(highest) and 5(lowest)")
    val input3 = readlnOrNull()
    try {
        val result = input3!!.toInt()
        require(result in 1..5)
        return result
    } catch (e: NumberFormatException) {
        colourPrintLn("value cannot be converted to number, returning to main menu", Colour.RED)
    } catch (e: IllegalArgumentException) {
        colourPrintLn("value must be between 1 and 5 inclusive", Colour.RED)
    }
    return null
}

fun colourPrintLn(text: String, colour: Colour) {
    colourPrint(text, colour)
    println()
}

fun colourPrint(text:String, colour:Colour) {
    val escapeSequence = "\u001B["
    val whiteText = "0m"
    val colourSequence = when (colour) {
        Colour.RED -> "31m"
        Colour.YELLOW -> "33m"
        Colour.GREEN -> "32m"
        Colour.BLUE -> "34m"
        Colour.PURPLE -> "35m"
    }
    if (Settings.colourFormat) {
        print(
            "$escapeSequence$colourSequence" +
                    text +
                    "$escapeSequence$whiteText"
        )
    } else {
        print(text)
    }
}


fun saveList(){
    try {
        FileWriter(File("list.txt")).use { it.write("hello") }
    } catch (e: IOException) {
        println("an error occurred writing to file")
    }
}