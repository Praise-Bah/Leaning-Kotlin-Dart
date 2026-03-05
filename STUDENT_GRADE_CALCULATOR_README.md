# Student Grade Calculator

This project implements a Student Grade Calculator that processes Excel files containing student names and scores, calculates their grades, and outputs the results to a new Excel file.

## Features Implemented

### Kotlin Concepts Applied (from Class)

1. **Lambdas and Higher-Order Functions**
   - Lambda expression for grade calculation: `{ score -> ... }`
   - Higher-order functions: `processStudents()`, `filter()`, `map()`, `forEach()`
   - Function parameters: `gradeFunction: (Double?) -> String`

2. **Abstract Classes**
   - `FileProcessor` - Abstract base class defining contract for file operations
   - Contains abstract methods: `readFile()` and `writeFile()`

3. **Interfaces**
   - `Processable` - Interface with abstract method `process()` and property `description`
   - Demonstrates polymorphism and contract definition

4. **Data Classes**
   - `Student` - Input data class
   - `StudentWithGrade` - Output data class with calculated grades

5. **Collection Operations with Lambdas**
   - `filter { it.score != null && it.score >= 60 }` - Filter passing students
   - `map { student -> StudentWithGrade(...) }` - Transform data
   - `groupBy { it.grade }` - Group students by grade
   - `forEach { ... }` - Iterate with lambda

## How It Works

1. **Input**: Excel file with columns:
   - Column A: Student Name
   - Column B: Score (0-100)

2. **Processing**:
   - Reads Excel file using Apache POI
   - Applies grade calculation using lambda expressions
   - Filters and transforms data using higher-order functions
   - Calculates grade distribution

3. **Output**: Excel file with columns:
   - Column A: Student Name
   - Column B: Score
   - Column C: Grade (A, B, C, D, F)

## Grade Scale

- **A**: 90-100
- **B**: 80-89
- **C**: 70-79
- **D**: 60-69
- **F**: Below 60

## Setup

1. **Dependencies**: Apache POI libraries are added in `build.gradle.kts`
   ```kotlin
   implementation("org.apache.poi:poi:5.2.5")
   implementation("org.apache.poi:poi-ooxml:5.2.5")
   ```

2. **Input File**: Create an Excel file anywhere on your computer with format:
   ```
   | Student Name | Score |
   |--------------|-------|
   | Alice        | 95    |
   | Bob          | 82    |
   | Charlie      | 67    |
   ```

## Usage

You can run the application in three ways:

### Option 1: Interactive Mode (No Arguments)
Run the program without arguments and enter the file path when prompted:
```bash
# Run the program
kotlin StudentGradeCalculator.kt

# You'll be prompted:
Enter input Excel file path: C:\Users\YourName\Documents\students.xlsx
```

### Option 2: Command-Line Argument (Input File Only)
Provide the input file path as an argument. The output file will be created automatically in the same directory with `_grades` suffix:
```bash
kotlin StudentGradeCalculator.kt "C:\Users\YourName\Documents\students.xlsx"
# Output: C:\Users\YourName\Documents\students_grades.xlsx
```

### Option 3: Command-Line Arguments (Input and Output)
Provide both input and output file paths:
```bash
kotlin StudentGradeCalculator.kt "C:\Users\YourName\Documents\students.xlsx" "C:\Users\YourName\Desktop\results.xlsx"
```

## Output File

The output file will be automatically created with:
- **Automatic naming**: If you don't specify output path, it adds `_grades` to the input filename
- **Same location**: Output file is created in the same directory as input file
- **Example**: 
  - Input: `C:\Documents\students.xlsx`
  - Output: `C:\Documents\students_grades.xlsx`

## Code Structure

- **Interface**: `Processable` - Defines processing contract
- **Abstract Class**: `FileProcessor` - Base class for file operations
- **Concrete Class**: `ExcelFileProcessor` - Implements Excel file I/O
- **Calculator Class**: `GradeCalculator` - Contains lambda-based grade logic
- **App Class**: `StudentGradeCalculatorApp` - Main application orchestrator

## Example Output

```
=== Student Grade Calculator ===

Reading students from: students_input.xlsx
Found 5 students

Calculating grades...

=== Results ===
Alice: Score = 95.0, Grade = A
Bob: Score = 82.0, Grade = B
Charlie: Score = 67.0, Grade = D
Diana: Score = 45.0, Grade = F
Eve: Score = 88.0, Grade = B

=== Passing Students (Score >= 60) ===
Alice: 95.0 - A
Bob: 82.0 - B
Charlie: 67.0 - D
Eve: 88.0 - B

=== Grade Distribution ===
Grade A: 1 student(s)
Grade B: 2 student(s)
Grade D: 1 student(s)
Grade F: 1 student(s)

Writing results to: students_output.xlsx
Output file created: students_output.xlsx

=== Processing Complete ===
```

## Creating Sample Input File

You can create a sample Excel file manually or use the Python script provided in `create_sample_excel.py` (if you have Python installed).

### Manual Creation:
1. Open Excel or Google Sheets
2. Create headers: "Student Name" (A1), "Score" (B1)
3. Add student data in rows below
4. Save as `students_input.xlsx`

## Notes

- The application expects the input file to be in the project root directory
- Output file will be created in the same directory
- Invalid scores (< 0 or > 100) are marked as "Invalid"
- Missing scores are marked as "No Score"
