# Java-Expense-Tracker

Expense Tracker is a simple Java-based tool to record daily expenses and income.
It stores data in a text file, shows monthly summaries, and uses a clean
menu-based interface. Beginner-friendly, easy to understand, and useful for
learning Java programming, file handling, classes, objects, and basic Java logic.

Submitted by: [Arni Halwe]
Registration Number: [25BAR10009]
College: [VIT-BPL]
Submission Date: [SEP-18-2026]


# Objective :

"To design and implement a simple Expense Tracker application using Java
that allows users to record and analyze their financial transactions in
an easy and organised manner."

1. To create a simple and beginner-friendly Java application for tracking
   daily expenses and income.

2. To store financial records and encourage better financial awareness and
   money-management habits.

3. To help users view and manage all past transactions in one place.

4. To generate a monthly summary showing total income, expenses, and net balance.


# Features

1. Add Expense -> Record amount, category, date, and an optional note.

2. Add Income -> Record income using the same structure as expenses.

3. View All Records -> Displays every entry stored so far.

4. Monthly Summary -> Shows total income, total expense, and net balance
   for a selected month.

5. Auto Data Saving -> Automatically stores all records in a text file
   named expenses.txt.

6. Very Simple UI -> Fully text-based and works on any system with Java installed.


# Tech Used:

1. Java

2. File Handling using Java NIO (java.nio.file.Files and Path)

3. ArrayList for storing records

4. Scanner for console input/output

5. LocalDate for handling dates

6. Classes and Objects for representing financial records

7. Exception Handling for handling invalid input and file errors

8. No external libraries required


# Steps to Run the Project:

1. Install Java JDK on your system.

2. Download or clone the project folder.

3. Open the project folder in VS Code or a terminal/command prompt.

4. Ensure ExpenseTracker.java is inside the project folder.

5. Open the terminal in that folder location.

6. Compile the program using:

   javac ExpenseTracker.java

7. Run the program using:

   java ExpenseTracker

8. The menu will appear on your screen.

9. Choose an option and follow the instructions to add or view records.

10. The application will automatically create and update expenses.txt
    when records are added.
