import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

// Interface: Defines abstract methods for processing operations
interface Processable {
    fun process()
    val description: String
}

// Abstract class: Defines contract for file processors
abstract class FileProcessor {
    abstract fun readFile(filePath: String): List<Student>
    abstract fun writeFile(filePath: String, students: List<StudentWithGrade>)
    
    fun description() = "This is a file processor"
}

// Data class for student input
data class Student(
    val name: String,
    val score: Double?
)

// Data class for student output with grade
data class StudentWithGrade(
    val name: String,
    val score: Double?,
    val grade: String
)

// Concrete implementation of FileProcessor for Excel files
class ExcelFileProcessor : FileProcessor(), Processable {
    override val description: String = "Excel file processor for student grades"
    
    override fun readFile(filePath: String): List<Student> {
        val students = mutableListOf<Student>()
        val file = File(filePath)
        
        if (!file.exists()) {
            println("Input file not found: $filePath")
            return emptyList()
        }
        
        FileInputStream(file).use { fis ->
            val workbook = XSSFWorkbook(fis)
            val sheet = workbook.getSheetAt(0)
            
            // Skip header row, start from row 1
            for (i in 1 until sheet.physicalNumberOfRows) {
                val row = sheet.getRow(i) ?: continue
                
                val nameCell = row.getCell(0)
                val scoreCell = row.getCell(1)
                
                val name = nameCell?.stringCellValue ?: continue
                val score = when (scoreCell?.cellType) {
                    CellType.NUMERIC -> scoreCell.numericCellValue
                    CellType.STRING -> scoreCell.stringCellValue.toDoubleOrNull()
                    else -> null
                }
                
                students.add(Student(name, score))
            }
            
            workbook.close()
        }
        
        return students
    }
    
    override fun writeFile(filePath: String, students: List<StudentWithGrade>) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Student Grades")
        
        // Create header row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Student Name")
        headerRow.createCell(1).setCellValue("Score")
        headerRow.createCell(2).setCellValue("Grade")
        
        // Fill data rows
        students.forEachIndexed { index, student ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(student.name)
            row.createCell(1).setCellValue(student.score ?: 0.0)
            row.createCell(2).setCellValue(student.grade)
        }
        
        // Auto-size columns
        for (i in 0..2) {
            sheet.autoSizeColumn(i)
        }
        
        FileOutputStream(filePath).use { fos ->
            workbook.write(fos)
        }
        
        workbook.close()
        println("Output file created: $filePath")
    }
    
    override fun process() {
        println("Processing Excel files for student grades...")
    }
}

// Grade calculator using lambdas and higher-order functions
class GradeCalculator {
    
    // Lambda expression for grade calculation
    private val gradeCalculator: (Double?) -> String = { score ->
        when {
            score == null -> "No Score"
            score < 0 || score > 100 -> "Invalid"
            score >= 90 -> "A"
            score >= 80 -> "B"
            score >= 70 -> "C"
            score >= 60 -> "D"
            else -> "F"
        }
    }
    
    // Higher-order function: takes a function as parameter
    fun processStudents(
        students: List<Student>,
        gradeFunction: (Double?) -> String = gradeCalculator
    ): List<StudentWithGrade> {
        // Using map (higher-order function) with lambda
        return students.map { student ->
            StudentWithGrade(
                name = student.name,
                score = student.score,
                grade = gradeFunction(student.score)
            )
        }
    }
    
    // Higher-order function: filtering with lambda
    fun filterPassingStudents(students: List<StudentWithGrade>): List<StudentWithGrade> {
        // Lambda expression: filter students with score > 60
        return students.filter { it.score != null && it.score >= 60 }
    }
    
    // Higher-order function: transforming data
    fun getStudentSummary(students: List<StudentWithGrade>): Map<String, Int> {
        return students
            .filter { it.score != null }
            .groupBy { it.grade }
            .mapValues { it.value.size }
    }
}

// Main application class
class StudentGradeCalculatorApp {
    private val processor = ExcelFileProcessor()
    private val calculator = GradeCalculator()
    
    fun run(inputPath: String, outputPath: String) {
        println("=== Student Grade Calculator ===\n")
        
        // Step 1: Read students from Excel
        println("Reading students from: $inputPath")
        val students = processor.readFile(inputPath)
        
        if (students.isEmpty()) {
            println("No students found in input file.")
            return
        }
        
        println("Found ${students.size} students\n")
        
        // Step 2: Calculate grades using lambdas and higher-order functions
        println("Calculating grades...")
        val studentsWithGrades = calculator.processStudents(students)
        
        // Step 3: Display results
        println("\n=== Results ===")
        studentsWithGrades.forEach { student ->
            val scoreDisplay = student.score?.toString() ?: "N/A"
            println("${student.name}: Score = $scoreDisplay, Grade = ${student.grade}")
        }
        
        // Step 4: Filter passing students (demonstration of lambda filtering)
        println("\n=== Passing Students (Score >= 60) ===")
        val passingStudents = calculator.filterPassingStudents(studentsWithGrades)
        passingStudents.forEach { student ->
            println("${student.name}: ${student.score} - ${student.grade}")
        }
        
        // Step 5: Grade distribution summary
        println("\n=== Grade Distribution ===")
        val summary = calculator.getStudentSummary(studentsWithGrades)
        summary.forEach { (grade, count) ->
            println("Grade $grade: $count student(s)")
        }
        
        // Step 6: Write results to Excel
        println("\nWriting results to: $outputPath")
        processor.writeFile(outputPath, studentsWithGrades)
        
        println("\n=== Processing Complete ===")
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("=== Student Grade Calculator ===")
        println("\nUsage: Provide the input Excel file path")
        println("Example: StudentGradeCalculator C:\\path\\to\\students.xlsx")
        println("\nOr enter the file path when prompted:")
        print("\nEnter input Excel file path: ")
        
        val inputPath = readLine()?.trim()
        
        if (inputPath.isNullOrEmpty()) {
            println("Error: No file path provided")
            return
        }
        
        val outputPath = generateOutputPath(inputPath)
        
        val app = StudentGradeCalculatorApp()
        app.run(inputPath, outputPath)
    } else {
        val inputPath = args[0]
        val outputPath = if (args.size > 1) args[1] else generateOutputPath(inputPath)
        
        val app = StudentGradeCalculatorApp()
        app.run(inputPath, outputPath)
    }
}

fun generateOutputPath(inputPath: String): String {
    val file = File(inputPath)
    val directory = file.parent ?: ""
    val nameWithoutExtension = file.nameWithoutExtension
    val extension = file.extension
    
    return if (directory.isEmpty()) {
        "${nameWithoutExtension}_grades.$extension"
    } else {
        "$directory${File.separator}${nameWithoutExtension}_grades.$extension"
    }
}
