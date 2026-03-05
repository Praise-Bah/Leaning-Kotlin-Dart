whatsdata class Student(
    val name: String,
    val score: Int?
)

fun calculateGrade(score: Int?): String {
    return when {
        score == null -> "No score provided"
        score < 0 || score > 100 -> "Invalid score"
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        score >= 60 -> "D"
        else -> "F"
    }
}

fun main() {
    println("=== Student Grade Calculator ===\n")

    val student1 = Student("Alice", 95)
    val student2 = Student("Bob", 82)
    val student3 = Student("Charlie", 67)
    val student4 = Student("Diana", 45)
    val student5 = Student("Eve", null)
    val student6 = Student("Frank", -10)
    val student7 = Student("Grace", 150)

    val students = listOf(student1, student2, student3, student4, student5, student6, student7)

    for (student in students) {
        val grade = calculateGrade(student.score)
        val scoreDisplay = student.score?.toString() ?: "N/A"
        println("${student.name}: Score = $scoreDisplay, Grade = $grade")
    }

    println("\n=== Edge Cases Demonstration ===")

    val nullStudent: Student? = null
    val safeName = nullStudent?.name ?: "Unknown Student"
    println("Safe call with Elvis: $safeName")

    val studentWithNullScore = Student("John", null)
    val scoreWithDefault = studentWithNullScore.score ?: 0
    println("${studentWithNullScore.name}'s score with default: $scoreWithDefault")

    val validStudent = Student("Sarah", 88)
    validStudent.score?.let {
        println("${validStudent.name} has a valid score: $it")
    }

    studentWithNullScore.score?.let {
        println("This won't print because score is null")
    } ?: println("${studentWithNullScore.name} has no score recorded")
}
