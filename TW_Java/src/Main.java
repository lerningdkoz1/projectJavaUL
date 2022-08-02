import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (!line.equals("exit")) {
            System.out.println("Введите число и знак по примеру \"5 + 5\". \n" +
                    "Для выхода введите сочетание букв: \"exit\"");
            line = scanner.nextLine();
            if (line.equals("exit"))
                continue;
            System.out.println(calc(line));
        }
    }

    public static String calc(String input) throws Exception {
        Calculator calculator = Calculator.ofString(input);
        return calculator.getResult();
    }
}
