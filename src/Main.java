import java.util.Scanner;

public class Main {

    private static final double ANNUAL_RATE_PERCENT = 7.5;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("===== Microfinance Savings Interest Calculator =====");
        System.out.println("Annual interest rate: " + ANNUAL_RATE_PERCENT + "%");

        int customerCount = readWholeNumber(input, "How many customers? ");
        String[] names = new String[customerCount];
        int[] deposits = new int[customerCount];
        double[] interests = new double[customerCount];

        for (int i = 0; i < customerCount; i++) {
            System.out.println("\nCustomer " + (i + 1));
            System.out.print("Name: ");
            names[i] = input.nextLine().trim();
            deposits[i] = readWholeNumber(input, "Deposit in UGX (whole number): ");
            interests[i] = calculateInterest(deposits[i]);
        }

        printSummaryReport(names, deposits, interests);
        demonstrateConversions();
        input.close();
    }

    // IMPLICIT conversion: int * double is automatically widened to double (safe)
    private static double calculateInterest(int deposit) {
        double interest = deposit * ANNUAL_RATE_PERCENT / 100;
        return interest;
    }

    private static void printSummaryReport(String[] names, int[] deposits, double[] interests) {
        long totalDeposits = 0;          // long: a total of many int deposits can exceed int range
        double totalExact = 0;
        long totalRounded = 0;
        long totalTruncated = 0;

        System.out.println("\n===== SUMMARY REPORT FOR MANAGEMENT =====");
        System.out.printf("%-12s %12s %14s %12s %12s%n",
                "Customer", "Deposit", "Exact interest", "Truncated", "Rounded");

        for (int i = 0; i < names.length; i++) {
            double exact = interests[i];
            int truncated = (int) exact;                  // EXPLICIT cast: drops the decimals
            int rounded = (int) Math.round(exact);        // EXPLICIT cast of the rounded long value

            System.out.printf("%-12s %,12d %,14.2f %,12d %,12d%n",
                    names[i], deposits[i], exact, truncated, rounded);

            totalDeposits += deposits[i];                 // IMPLICIT: int widened to long
            totalExact += exact;
            totalTruncated += truncated;
            totalRounded += rounded;
        }

        System.out.println("---------------------------------------------------------------------");
        System.out.printf("%-12s %,12d %,14.2f %,12d %,12d%n",
                "TOTAL", totalDeposits, totalExact, totalTruncated, totalRounded);
        System.out.printf("Interest lost by truncating: %,.2f UGX%n", totalExact - totalTruncated);
        System.out.printf("Rounding difference        : %,.2f UGX%n", totalExact - totalRounded);
    }

    private static void demonstrateConversions() {
        System.out.println("\n===== TYPE CONVERSION DEMONSTRATION =====");

        System.out.println("-- Implicit (automatic) conversions: small type -> bigger type --");
        int whole = 1234;
        long asLong = whole;
        double asDouble = whole;
        System.out.println("int " + whole + " -> long " + asLong + " -> double " + asDouble + " (safe, no data lost)");

        System.out.println("\n-- Explicit (manual) conversions: bigger type -> smaller type --");
        double exactInterest = 1234.99;
        int truncated = (int) exactInterest;
        long rounded = Math.round(exactInterest);
        System.out.println("(int) " + exactInterest + " = " + truncated + "  (decimals cut off)");
        System.out.println("Math.round(" + exactInterest + ") = " + rounded + "  (proper rounding)");

        int big = 300;
        byte small = (byte) big;
        System.out.println("(byte) " + big + " = " + small + "  (overflow: value does not fit in a byte)");

        long hugeDeposit = 5_000_000_000L;
        int shrunk = (int) hugeDeposit;
        System.out.println("(int) " + hugeDeposit + " = " + shrunk + "  (long value too large for int)");

        System.out.println("\n-- Integer division pitfall --");
        int shillings = 10;
        int months = 3;
        double wrong = shillings / months;
        double right = (double) shillings / months;
        System.out.println("10 / 3 as int division = " + wrong + "  (decimals lost BEFORE assignment)");
        System.out.println("(double) 10 / 3         = " + right);

        System.out.println("\n-- Implicit does not always mean lossless --");
        int largeDeposit = 16_777_217;
        float asFloat = largeDeposit;
        System.out.println("int " + largeDeposit + " -> float -> back to int = " + (int) asFloat
                + "  (float cannot hold every int exactly)");
    }

    private static int readWholeNumber(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("Please enter a positive whole number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a whole number without decimals or letters.");
            }
        }
    }
}
