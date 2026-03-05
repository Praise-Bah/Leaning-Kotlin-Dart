import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

// ── Interface (defines what a gradable item can do) ──
interface Gradable {
    fun calculateGrade(): String
}

// ── Abstract class (base for any person) ──
abstract class Person(val name: String) {
    abstract fun display(): String
}

// ── Student class: extends Person, implements Gradable ──
data class Student(
    val studentName: String,
    val score: Int?
) : Person(studentName), Gradable {

    override fun calculateGrade(): String = when {
        score == null -> "N/A"
        score < 0 || score > 100 -> "Invalid"
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        score >= 60 -> "D"
        else -> "F"
    }

    override fun display(): String {
        val scoreText = score?.toString() ?: "N/A"
        return "$name | Score: $scoreText | Grade: ${calculateGrade()}"
    }
}

// ── Higher-order function (takes a lambda) ──
fun processStudents(students: List<Student>, action: (Student) -> Unit) {
    students.forEach { action(it) }
}

// ── Read students from an Excel file (.xlsx) ──
fun readStudentsFromExcel(filePath: String): List<Student> {
    val students = mutableListOf<Student>()
    FileInputStream(File(filePath)).use { fis ->
        val workbook = XSSFWorkbook(fis)
        val sheet = workbook.getSheetAt(0)
        for (i in 1..sheet.lastRowNum) {
            val row = sheet.getRow(i) ?: continue
            val name = row.getCell(0)?.stringCellValue ?: continue
            val score = row.getCell(1)?.numericCellValue?.toInt()
            students.add(Student(name, score))
        }
        workbook.close()
    }
    return students
}

// ── Write students with grades to a new Excel file (.xlsx) ──
fun writeStudentsToExcel(students: List<Student>, filePath: String) {
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("Student Grades")

    // Header row
    val header = sheet.createRow(0)
    header.createCell(0).setCellValue("Name")
    header.createCell(1).setCellValue("Score")
    header.createCell(2).setCellValue("Grade")

    // Data rows using lambda with mapIndexed
    students.mapIndexed { index, student ->
        val row = sheet.createRow(index + 1)
        row.createCell(0).setCellValue(student.name)
        row.createCell(1).setCellValue((student.score ?: 0).toDouble())
        row.createCell(2).setCellValue(student.calculateGrade())
    }

    FileOutputStream(File(filePath)).use { fos ->
        workbook.write(fos)
    }
    workbook.close()
    println("Output written to: $filePath")
}

// ── Create a sample input Excel file for testing ──
fun createSampleInput(filePath: String) {
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet("Students")

    val header = sheet.createRow(0)
    header.createCell(0).setCellValue("Name")
    header.createCell(1).setCellValue("Score")

    val sampleData = listOf("Alice" to 95, "Bob" to 82, "Charlie" to 67, "Diana" to 45, "Eve" to 73, "Frank" to 91)
    sampleData.forEachIndexed { index, (name, score) ->
        val row = sheet.createRow(index + 1)
        row.createCell(0).setCellValue(name)
        row.createCell(1).setCellValue(score.toDouble())
    }

    FileOutputStream(File(filePath)).use { fos ->
        workbook.write(fos)
    }
    workbook.close()
    println("Sample input file created: $filePath")
}

// ── Main ──
fun main() {
    val inputFile = "students_input.xlsx"
    val outputFile = "students_output.xlsx"

    // Create a sample input file if it doesn't exist
    if (!File(inputFile).exists()) {
        println("No input file found. Creating sample: $inputFile")
        createSampleInput(inputFile)
    }

    println("=== Student Grade Calculator ===\n")

    // Read students from Excel
    val students = readStudentsFromExcel(inputFile)

    // Display all students using higher-order function + lambda
    println("All Students:")
    processStudents(students) { println("  ${it.display()}") }

    // Filter & map using lambdas (from class slides)
    val passingStudents = students.filter { (it.score ?: 0) >= 60 }
    val failingStudents = students.filter { (it.score ?: 0) < 60 }

    println("\nPassing Students:")
    passingStudents.map { it.name }.forEach { println("  $it") }

    println("\nFailing Students:")
    failingStudents.map { it.name }.forEach { println("  $it") }

    // Write results to output Excel file
    println()
    writeStudentsToExcel(students, outputFile)

    println("\nDone! Open '$outputFile' in Excel to see the grades.")
}
