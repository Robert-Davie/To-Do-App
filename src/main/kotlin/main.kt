import java.io.File
import java.io.FileWriter
import java.io.IOException


const val ESCAPE_SEQUENCE = "\u001B["
const val RED_TEXT = "31m"
const val BLACK_TEXT = "0m"
const val GREEN_TEXT = "32m"
const val BLUE_TEXT = "34m"

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

fun addTask(dataIn: ListData) {
    println("new task name")
    val newName = readlnOrNull()
    if (newName == "" || newName == null) {
        colourPrintLn("name cannot be empty, returning to menu", "RED")
        return
    }
    println("new task description [optional]")
    val newDescription = readlnOrNull()
    println("task priority, pick number between 1(highest) and 5(lowest)")
    val input3 = readlnOrNull()
    try {
        require(input3!!.toInt() in 1..5)
    } catch (e: NumberFormatException) {
        colourPrintLn("value cannot be converted to number, returning to main menu", "RED")
        return
    } catch (e: IllegalArgumentException) {
        colourPrintLn("value must be between 1 and 5 inclusive","RED")
    }
    println("add due date [y/n]")
    if (readlnOrNull() == "y") {
        println("adding dates not yet implemented")
    }
    val task = Task(
        iD = dataIn.currentIDCount + 1,
        name = newName,
        description = newDescription,
        priority = input3!!.toInt(),
        complete = false,
        dueDate = null
    )
    dataIn.addTask(task)
}


fun colourPrintLn(text:String, colour:String?) {
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