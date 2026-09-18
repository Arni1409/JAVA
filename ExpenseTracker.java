import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExpenseTracker {

    // File where all expense and income records are stored
    private static final String FILE_NAME = "expenses.txt";

    // Scanner for taking input from the user
    private static final Scanner scanner = new Scanner(System.in);


    // ---------------------------------------------------------
    // RECORD CLASS
    // ---------------------------------------------------------

    static class Record {

        String type;
        double amount;
        String category;
        String date;
        String note;

        Record(String type, double amount, String category,
               String date, String note) {

            this.type = type;
            this.amount = amount;
            this.category = category;
            this.date = date;
            this.note = note;
        }
    }


    // ---------------------------------------------------------
    // LOAD DATA FROM FILE
    // ---------------------------------------------------------

    static ArrayList<Record> loadData() {

        ArrayList<Record> records = new ArrayList<>();

        Path file = Path.of(FILE_NAME);

        // If the file doesn't exist, simply return an empty list
        if (!Files.exists(file)) {
            return records;
        }

        try {

            ArrayList<String> lines = new ArrayList<>(Files.readAllLines(file));

            for (String line : lines) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|", -1);

                // Every record should contain exactly 5 parts
                if (parts.length != 5) {
                    continue;
                }

                try {

                    String type = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    String date = parts[3];
                    String note = parts[4];

                    Record record = new Record(
                            type,
                            amount,
                            category,
                            date,
                            note
                    );

                    records.add(record);

                } catch (NumberFormatException e) {

                    // Ignore damaged records instead of crashing
                }
            }

        } catch (IOException e) {

            System.out.println("Unable to read expense data.");
        }

        return records;
    }


    // ---------------------------------------------------------
    // SAVE DATA TO FILE
    // ---------------------------------------------------------

    static void saveData(ArrayList<Record> records) {

        ArrayList<String> lines = new ArrayList<>();

        for (Record record : records) {

            String line =
                    cleanText(record.type) + "|" +
                    record.amount + "|" +
                    cleanText(record.category) + "|" +
                    record.date + "|" +
                    cleanText(record.note);

            lines.add(line);
        }

        try {

            Files.write(
                    Path.of(FILE_NAME),
                    lines
            );

        } catch (IOException e) {

            System.out.println("Unable to save expense data.");
        }
    }


    // ---------------------------------------------------------
    // CLEAN TEXT
    // ---------------------------------------------------------

    static String cleanText(String text) {

        if (text == null) {
            return "";
        }

        // The | symbol is used to separate fields,
        // so replace it if the user enters it.
        return text.replace("|", "/");
    }


    // ---------------------------------------------------------
    // ADD EXPENSE / INCOME
    // ---------------------------------------------------------

    static void addEntry(String entryType) {

        System.out.println("\nAdding a new "
                + entryType.toUpperCase() + " record:");

        // ---------------- AMOUNT ----------------

        System.out.print("Amount: ");

        String amountInput = scanner.nextLine().trim();

        double amount;

        try {

            amount = Double.parseDouble(amountInput);

            if (amount <= 0) {

                System.out.println("Amount must be greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {

            System.out.println("Amount must be a valid number.");
            return;
        }


        // ---------------- CATEGORY ----------------

        System.out.print("Category (Food, Travel, etc.): ");

        String category = scanner.nextLine().trim();

        if (category.isEmpty()) {
            category = "General";
        }


        // ---------------- DATE ----------------

        System.out.print(
                "Date (YYYY-MM-DD) [Press enter for today]: "
        );

        String dateString = scanner.nextLine().trim();

        if (dateString.isEmpty()) {

            dateString = LocalDate.now().toString();

        } else {

            // Check whether the entered date is valid
            try {

                LocalDate.parse(dateString);

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Please use YYYY-MM-DD format."
                );

                return;
            }
        }


        // ---------------- NOTE ----------------

        System.out.print("Any note (optional): ");

        String note = scanner.nextLine().trim();


        // ---------------- CREATE RECORD ----------------

        Record record = new Record(
                entryType,
                amount,
                category,
                dateString,
                note
        );


        // ---------------- SAVE RECORD ----------------

        ArrayList<Record> records = loadData();

        records.add(record);

        saveData(records);

        System.out.println("Record saved successfully!");
    }


    // ---------------------------------------------------------
    // SHOW ALL RECORDS
    // ---------------------------------------------------------

    static void showAll() {

        ArrayList<Record> records = loadData();

        if (records.isEmpty()) {

            System.out.println("\nNo records found yet.");
            return;
        }

        System.out.println("\n--- All Records ---");

        int number = 1;

        for (Record record : records) {

            System.out.println(
                    number + ". "
                    + record.date + " | "
                    + record.type + " | ₹"
                    + String.format("%.2f", record.amount)
                    + " | "
                    + record.category + " | "
                    + record.note
            );

            number++;
        }
    }


    // ---------------------------------------------------------
    // MONTHLY SUMMARY
    // ---------------------------------------------------------

    static void monthlySummary() {

        System.out.print("Enter month (YYYY-MM): ");

        String month = scanner.nextLine().trim();

        if (month.isEmpty()) {

            System.out.println("Please enter a valid month.");
            return;
        }


        // Check month format
        try {

            if (month.length() != 7) {
                throw new DateTimeParseException(
                        "Invalid month",
                        month,
                        0
                );
            }

            int year = Integer.parseInt(month.substring(0, 4));
            int monthNumber = Integer.parseInt(month.substring(5, 7));

            if (month.charAt(4) != '-'
                    || monthNumber < 1
                    || monthNumber > 12
                    || year < 1) {

                throw new DateTimeParseException(
                        "Invalid month",
                        month,
                        0
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Invalid month. Please use YYYY-MM format."
            );

            return;
        }


        // ---------------- CALCULATE TOTALS ----------------

        ArrayList<Record> records = loadData();

        double totalIncome = 0;
        double totalExpense = 0;

        for (Record record : records) {

            if (record.date.startsWith(month)) {

                if (record.type.equals("income")) {

                    totalIncome += record.amount;

                } else if (record.type.equals("expense")) {

                    totalExpense += record.amount;
                }
            }
        }


        // ---------------- DISPLAY SUMMARY ----------------

        System.out.println("\n--- Monthly Summary ---");

        System.out.println("Month: " + month);

        System.out.println(
                "Total Income : ₹"
                + String.format("%.2f", totalIncome)
        );

        System.out.println(
                "Total Expense: ₹"
                + String.format("%.2f", totalExpense)
        );

        System.out.println(
                "Net Balance  : ₹"
                + String.format(
                        "%.2f",
                        totalIncome - totalExpense
                )
        );
    }


    // ---------------------------------------------------------
    // MAIN MENU
    // ---------------------------------------------------------

    static void menu() {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("     SIMPLE EXPENSE TRACKER");
            System.out.println("==============================");

            System.out.println("1) Add Expense");
            System.out.println("2) Add Income");
            System.out.println("3) View All Records");
            System.out.println("4) Monthly Summary");
            System.out.println("0) Exit");

            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();


            if (choice.equals("1")) {

                addEntry("expense");

            } else if (choice.equals("2")) {

                addEntry("income");

            } else if (choice.equals("3")) {

                showAll();

            } else if (choice.equals("4")) {

                monthlySummary();

            } else if (choice.equals("0")) {

                System.out.println("Goodbye!");
                break;

            } else {

                System.out.println(
                        "Invalid choice. Please try again."
                );
            }
        }
    }


    // ---------------------------------------------------------
    // PROGRAM STARTS HERE
    // ---------------------------------------------------------

    public static void main(String[] args) {

        menu();

        scanner.close();
    }
}