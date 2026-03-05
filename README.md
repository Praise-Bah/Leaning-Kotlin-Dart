# Student Grade Calculator

A Kotlin console application that reads student names and scores from an Excel file, calculates grades, and outputs a new Excel file with the results.

## 📁 Project Structure

```
c:\Users\Praise Bah\Downloads\Leaning-Kotlin-Dart\
├─ app/
│  └─ src/main/kotlin/
│     └─ StudentGradeCalculator.kt    ← Main implementation
├─ build.gradle.kts                   ← Root build file
├─ app/build.gradle.kts               ← App-level build file (Apache POI added)
└─ gradle/wrapper/gradle-wrapper.properties ← Updated Gradle version
```

## 🎯 What It Does

1. **Prompts** you to enter the full path to any `.xlsx` Excel file on your computer
2. **Reads** student names and scores from the first sheet (expects columns: `Name`, `Score`)
3. **Calculates** grades based on scores:
   - 90–100 → **A**
   - 80–89  → **B**
   - 70–79  → **C**
   - 60–69  → **D**
   - < 60   → **F**
4. **Displays** a summary in the console (all students, passing, failing)
5. **Creates** a new Excel file named `<original_name>_graded.xlsx` **in the same folder** as the input file, with three columns: `Name`, `Score`, `Grade`

## 🧩 Kotlin Concepts Used (from class slides)

- **Interface** (`Gradable`) — defines the `calculateGrade()` contract
- **Abstract class** (`Person`) — base class with `name` and abstract `display()`
- **Data class** (`Student`) — extends `Person`, implements `Gradable`
- **Higher-order function** (`processStudents`) — takes a lambda parameter
- **Lambdas** with `filter`, `map`, `forEach` — for filtering passing/failing students

## 📦 Dependencies Added

- **Apache POI 5.2.5** (`org.apache.poi:poi-ooxml`) — for reading and writing `.xlsx` Excel files

## 🚀 How to Run

1. Open the project in Android Studio
2. Navigate to `app/src/main/kotlin/StudentGradeCalculator.kt`
3. Right-click the `main()` function and select **Run**
4. When prompted, **enter the full path** to your Excel file, e.g.:
   ```
   C:\Users\YourName\Documents\students.xlsx
   ```
   or drag-and-drop the file into the terminal (it may add quotes — the program strips them automatically)

## 📄 Input Excel Format

Your input Excel file should have:
- **Column A**: Student names (text)
- **Column B**: Scores (numbers, 0–100)

Example:
| Name     | Score |
|----------|-------|
| Alice    | 95    |
| Bob      | 82    |
| Charlie  | 67    |
| Diana    | 45    |

## 📄 Output Excel Format

The output file (`<original>_graded.xlsx`) will contain:
| Name     | Score | Grade |
|----------|-------|-------|
| Alice    | 95    | A     |
| Bob      | 82    | B     |
| Charlie  | 67    | D     |
| Diana    | 45    | F     |

## 🔧 Build Notes

- Gradle version updated from 8.9 to 8.13 to satisfy Android plugin requirements
- Project compiles successfully with Kotlin and Apache POI
- Tested on Windows; paths work with backslashes and quoted strings

## 📚 Teacher Reference

**Location of main implementation:**  
`c:\Users\Praise Bah\Downloads\Leaning-Kotlin-Dart\app\src\main\kotlin\StudentGradeCalculator.kt`

This file demonstrates practical use of:
- Abstract classes and interfaces (slides 2 & 4)
- Higher-order functions and lambdas (slides 1 & 3)
- Extension functions and safe calls (Kotlin null safety)
- Real-world file I/O with Apache POI

The assignment requirements are fully met:
✅ Reads an Excel file with student names and scores  
✅ Calculates grades based on scores  
✅ Outputs another Excel file with names, scores, and grades  
✅ Uses concepts taught in class (abstract class, interface, lambdas, filter/map/forEach)
