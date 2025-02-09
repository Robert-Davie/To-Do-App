import java.io.File
import java.io.FileWriter
import java.io.IOException


fun main(){
    val listData = ListData()
    listData.addDefaultTasks()
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
            0 to QUIT [or type q]
        """.trimIndent(), Colour.GREEN)
        colourPrintLn("=".repeat(60), Colour.BLUE)
        val userInput = readlnOrNull()
        when (userInput) {
            "1" -> {
                if (listData.tasks.none { !it.complete }) {
                    colourPrintLn("no incomplete tasks exist", Colour.RED)
                    continue
                }
                listData.prettyPrint(showComplete = false)
            }
            "2" -> {
                if (listData.tasks.none { it.complete }) {
                    colourPrintLn("no complete tasks exist", Colour.RED)
                    continue
                }
                listData.prettyPrint(showIncomplete = false)
            }
            "3" -> {
                markTaskComplete(listData)
            }
            "4" -> {
                addTask(listData)
            }
            "5" -> {
                viewTask(listData)
            }
            "6" -> {
                updateTask(listData)
            }
            "7" -> {
                deleteTask(listData)
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

enum class Colour {
    RED,
    YELLOW,
    GREEN,
    BLUE,
    PURPLE,
}

fun deleteTask(listData: ListData) {
    val taskNumber = chooseTask(
        listData,
        "Select number of which task to delete",
    )
    if (taskNumber != null) {
        listData.tasks.removeAt(taskNumber)
    }
}

fun viewTask(listData: ListData) {
    val taskNumber = chooseTask(
        listData,
        "Select number of which task to view",
        )
    println("=".repeat(40))
    if (taskNumber != null) {
        val task = listData.tasks[taskNumber]
        colourPrintLn("   task name: ${task.name}", Colour.BLUE)
        colourPrintLn("     task ID: ${task.iD}", Colour.BLUE)
        colourPrintLn(" description: ${task.description}", Colour.BLUE)
        colourPrintLn("    priority: ${task.priority}", Colour.BLUE)
        colourPrintLn("is complete?: ${task.complete}", Colour.BLUE)
        colourPrintLn("    due date: ${task.dueDate}", Colour.BLUE)
    }
}

fun markTaskComplete(listData: ListData) {
    val taskPosition = chooseTask(
        listData,
        "Select number of which task to mark as complete [or press q to quit]",
        showComplete = false
    )
    val tasks = listData.tasks.filter { !it.complete }
    if (taskPosition != null) {
        tasks[taskPosition].complete = true
    }
}

fun updateTask(listData: ListData) {
    val taskPosition = chooseTask(
        listData,
        "Select number of which of task to update [or press q to quit]"
    )
    if (taskPosition != null) {
        updateGivenTask(listData, taskPosition)
    }
}

fun chooseTask(listData: ListData, message: String, showComplete: Boolean = true): Int? {
    val tasks = if (showComplete) {
        listData.tasks
    } else {
        listData.tasks.filter { !it.complete } as MutableList<Task>
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

fun updateGivenTask(listData: ListData, position: Int) {
    val oldTask = listData.tasks[position]

    val currentName = oldTask.name
    println("change current name: $currentName [y/n]")
    if (readlnOrNull()=="y"){
        println("enter new name")
        listData.tasks[position].name = readlnOrNull()!!
    }

    val currentDescription = oldTask.description
    println("change current description: ${currentDescription ?: ""} [y/n]")
    if (readlnOrNull()=="y"){
        println("enter new description")
        listData.tasks[position].description = readlnOrNull()!!
    }

    val currentPriority = oldTask.priority
    println("change current priority: $currentPriority [y/n]")
    if (readlnOrNull()=="y") {
        val newPriority = getPriorityForTaskFromUser()
        if (newPriority == null) {
            colourPrintLn("priority invalid, returning to main menu", Colour.RED)
            return
        }
        listData.tasks[position].priority = newPriority
    }

    val currentComplete = oldTask.complete
    println("change current completeness: $currentComplete?[y/n]")
    if (readlnOrNull()=="y") {
        listData.tasks[position].complete = !currentComplete
    }

    println("change date not yet supported")
}

fun addTask(dataIn: ListData) {
    println("new task name")
    val newName = readlnOrNull()
    if (newName == "" || newName == null) {
        colourPrintLn("name cannot be empty, returning to menu", Colour.RED)
        return
    }
    println("new task description [optional]")
    val newDescription = readlnOrNull()
    val newPriority = getPriorityForTaskFromUser()
    if (newPriority == null){
        colourPrintLn("input not valid priority, return to main menu", Colour.RED)
        return
    }
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


fun colourPrintLn(text:String, colour:Colour) {
    val escapeSequence = "\u001B["
    val blackText = "0m"
    val colourSequence = when (colour) {
        Colour.RED -> "31m"
        Colour.YELLOW -> "33m"
        Colour.GREEN -> "32m"
        Colour.BLUE -> "34m"
        Colour.PURPLE -> "35m"
        else -> blackText
    }
    println("$escapeSequence$colourSequence" +
            text +
            "$escapeSequence$blackText")
}


fun saveList(){
    try {
        FileWriter(File("list.txt")).use { it.write("hello") }
    } catch (e: IOException) {
        println("an error occurred writing to file")
    }
}