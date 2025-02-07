import java.io.File
import java.io.FileWriter
import java.io.IOException


fun main(){
    val listData = ListData()
    listData.addDefaultTasks()
    while(true) {
        colourPrintLn("To do app menu", "GREEN")
        colourPrintLn("=".repeat(40), "BLUE")
        println("""
            press 1 to view active tasks
            press 2 to view complete tasks
            press 3 to add task
            press 4 to update task
            press 0 to quit
        """.trimIndent())
        val userInput = readlnOrNull()
        when (userInput) {
            "1" -> {
                listData.prettyPrint(showComplete = false)
            }
            "2" -> {
                listData.prettyPrint(showIncomplete = false)
            }
            "3" -> {
                addTask(listData)
            }
            "4" -> {
                updateTask(listData)
            }
            "0" -> {
                println("Application closed")
                return
            }
            else -> {
                colourPrintLn("user command $userInput unknown", "RED")
            }
        }
    }
}

fun updateTask(listData: ListData) {
    val listSize = listData.tasks.size
    for (i in 0..<listSize) {
        println("$i ${listData.tasks[i].name}")
    }
    while(true){
        colourPrintLn("Select number of which of task to update [or press q to quit]", "GREEN")
        val input = readlnOrNull()
        if (input == "q"){
            return
        }
        if(input?.toIntOrNull() in 0..<listSize){
            updateGivenTask(listData, input!!.toInt())
            return
        }
    }
}

fun updateGivenTask(listData: ListData, position: Int) {
    val oldTask = listData.tasks[position]

    var currentName = oldTask.name
    println("change current name: $currentName [y/n]")
    if (readlnOrNull()=="y"){
        println("enter new name")
        currentName = readlnOrNull()!!
    }

    var currentDescription = oldTask.description
    println("change current description: ${currentDescription ?: ""} [y/n]")
    if (readlnOrNull()=="y"){
        println("enter new description")
        currentDescription = readlnOrNull()!!
    }

    var currentPriority = oldTask.priority
    println("change current priority: $currentPriority [y/n]")
    if (readlnOrNull()=="y"){
        val newPriority = getPriorityForTaskFromUser()
        if (newPriority == null) {
            colourPrintLn("priority invalid, returning to main menu", "RED")
            return
        }
        currentPriority = newPriority
    }
}

fun addTask(dataIn: ListData) {
    println("new task name")
    val newName = readlnOrNull()
    if (newName == "" || newName == null) {
        colourPrintLn("name cannot be empty, returning to menu", "RED")
        return
    }
    println("new task description [optional]")
    val newDescription = readlnOrNull()
    val newPriority = getPriorityForTaskFromUser()
    if (newPriority == null){
        colourPrintLn("input not valid priority, return to main menu", "RED")
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
        colourPrintLn("value cannot be converted to number, returning to main menu", "RED")
    } catch (e: IllegalArgumentException) {
        colourPrintLn("value must be between 1 and 5 inclusive","RED")
    }
    return null
}


fun colourPrintLn(text:String, colour:String?) {
    val ESCAPE_SEQUENCE = "\u001B["
    val RED_TEXT = "31m"
    val BLACK_TEXT = "0m"
    val GREEN_TEXT = "32m"
    val BLUE_TEXT = "34m"
    val colourSequence = when (colour) {
        "RED" -> RED_TEXT
        "GREEN" -> GREEN_TEXT
        "BLUE" -> BLUE_TEXT
        else -> BLACK_TEXT
    }
    println("$ESCAPE_SEQUENCE$colourSequence$text$ESCAPE_SEQUENCE$BLACK_TEXT")
}


fun saveList(){
    try {
        FileWriter(File("list.txt")).use { it.write("hello") }
    } catch (e: IOException) {
        println("an error occurred writing to file")
    }
}