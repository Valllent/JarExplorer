import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.system.exitProcess

private const val DECOMPILED_FOLDER_NAME = "decompiled"

fun main(args: Array<String>) {
    if (args.size == 2) {
        val jarName = args[0]
        println("Jar name: $jarName")

        val searchWords = args[1]
        println("Search words: $searchWords")
        try {
            val decompiledFolder = File(DECOMPILED_FOLDER_NAME)
            if (decompiledFolder.exists().not()) {
                decompiledFolder.mkdir()
                decompile(jarName)
                unzip(jarName)
                deleteDecompiledJar(jarName)
            }
            findUsages(searchWords)
        } catch (exception: Exception) {
            System.err.println("Exception: ${exception.message}")
            deleteDecompiledFolder()
        }
    } else {
        System.err.println("Please enter 2 string parameters: jar name and search word")
    }
}

private fun decompile(jarName: String) {
    println("Decompiling...")
    // dgs - decompile generic signatures
    val commands = "-dgs=1 -log=WARN $jarName $DECOMPILED_FOLDER_NAME"
    val split = commands.split(" ")
    ConsoleDecompiler.main(split.toTypedArray())
}

private fun unzip(jarName: String) {
    println("Unzipping...")
    runCommandsLine("cmd.exe /c cd $DECOMPILED_FOLDER_NAME && jar xf $jarName")
}

private fun deleteDecompiledJar(jarName: String) {
    println("Deleting decompiled jar...")
    val file = File("$DECOMPILED_FOLDER_NAME/$jarName")
    file.delete()
}

private fun findUsages(searchWords: String) {
    println("Searching words...\n")
    val decompiledFolder = File(DECOMPILED_FOLDER_NAME)
    decompiledFolder.listFiles()?.forEach {
        parseFile(it, searchWords)
    }
    println()
}

private fun deleteDecompiledFolder() {
    System.err.println("Deleting decompiling folder...")
    File(DECOMPILED_FOLDER_NAME).delete()
}

private fun parseFile(file: File, searchWords: String) {
    when {
        file.isDirectory -> {
            file.listFiles()?.forEach {
                parseFile(it, searchWords)
            }
        }
        file.extension == "java" -> {
            var lineNumber = 1
            file.forEachLine {
                if (it.contains(searchWords)) {
                    println("\t(${file.name}:$lineNumber)")
                }
                lineNumber++
            }
        }
    }
}

private fun runCommandsLine(commandsLine: String) {
    val commands = commandsLine.split(" ")
    val process = ProcessBuilder(commands).run {
        redirectErrorStream(true)
        start()
    }

    val output = BufferedReader(InputStreamReader(process.inputStream))
    output.forEachLine {
        println(it)
    }

    val resultCode = process.waitFor()
    output.close()
    when {
        resultCode != 0 -> {
            System.err.println("Program exit with error!")
            exitProcess(1)
        }
    }
}