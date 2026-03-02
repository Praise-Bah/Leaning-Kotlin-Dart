// grade_calculator.dart

class Student {
  final String name;
  final int? score; // nullable score (like Kotlin Int?)

  Student(this.name, this.score);
}

String calculateGrade(int? score) {
  if (score == null) return "No score provided";
  if (score < 0 || score > 100) return "Invalid score";
  if (score >= 90) return "A";
  if (score >= 80) return "B";
  if (score >= 70) return "C";
  if (score >= 60) return "D";
  return "F";
}

/// A small helper to mimic Kotlin's `?.let { }` behavior.
/// If value is not null, it runs the action and returns the value; otherwise returns null.
T? letDo<T>(T? value, void Function(T v) action) {
  if (value != null) {
    action(value);
    return value;
  }
  return null;
}

void main() {
  print("=== Student Grade Calculator ===\n");

  final student1 = Student("Alice", 95);
  final student2 = Student("Bob", 82);
  final student3 = Student("Charlie", 67);
  final student4 = Student("Diana", 45);
  final student5 = Student("Eve", null);
  final student6 = Student("Frank", -10);
  final student7 = Student("Grace", 150);

  final students = [
    student1,
    student2,
    student3,
    student4,
    student5,
    student6,
    student7,
  ];

  for (final student in students) {
    final grade = calculateGrade(student.score);
    final scoreDisplay = student.score?.toString() ?? "N/A"; // safe access + default
    print("${student.name}: Score = $scoreDisplay, Grade = $grade");
  }

  print("\n=== Edge Cases Demonstration ===");

  Student? nullStudent;
  final safeName = nullStudent?.name ?? "Unknown Student";
  print("Safe call with default: $safeName");

  final studentWithNullScore = Student("John", null);
  final scoreWithDefault = studentWithNullScore.score ?? 0;
  print("${studentWithNullScore.name}'s score with default: $scoreWithDefault");

  final validStudent = Student("Sarah", 88);

  // Kotlin: validStudent.score?.let { println(...) }
  letDo<int>(validStudent.score, (v) {
    print("${validStudent.name} has a valid score: $v");
  });

  // Kotlin: studentWithNullScore.score?.let { ... } ?: println("no score")
  final printed = letDo<int>(studentWithNullScore.score, (v) {
    print("This won't print because score is null: $v");
  });
  if (printed == null) {
    print("${studentWithNullScore.name} has no score recorded");
  }
}
