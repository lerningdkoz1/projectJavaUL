import java.util.ArrayList;
import java.util.Arrays;

class Calculator {
    private int numberOne;
    private int numberTwo;
    private Operation operation;
    private Format format;

    private Calculator() {
    }

    public String getResult() throws Exception {
        int result = getIntermediateResult();
        if (format == Format.ROME) {
            return getRomeResult(result);
        }
        return String.valueOf(result);
    }

    private int getIntermediateResult() throws Exception {
        switch (operation) {
            case PLUS -> {
                return numberOne + numberTwo;
            }
            case MINUS -> {
                return numberOne - numberTwo;
            }
            case DIVIDE -> {
                if (numberTwo == 0) {
                    throw new Exception("Проверьте условие деления на ноль.");
                }
                return numberOne / numberTwo;
            }
            case MULTIPLY -> {
                return numberOne * numberTwo;
            }
        }
        throw new UnknownOperationException();
    }

    private String getRomeResult(int result) {
        if (result < 1) {
            throw new NegativeRomanNumberException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        RomanNumeral[] romanNumerals = RomanNumeral.values();
        while ((result > 0) && (i < romanNumerals.length)) {
            RomanNumeral currentSymbol = romanNumerals[i];
            if (currentSymbol.getValue() <= result) {
                stringBuilder.append(currentSymbol.name());
                result -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return String.valueOf(stringBuilder);
    }

    public static Calculator ofString(String line){
        String[] elements = line.split(" ");
        if (elements.length > 3) {
            throw new InvalidNumberOfOperandsException();
        }
        if (elements.length < 3) {
            throw new InvalidNumberOfOperandsException();
        }
        Calculator calculator = new Calculator();
        calculator.operation = Operation.bySign(elements[1]);
        try {
            calculator.numberOne = romanToArabic(elements[0]);
            calculator.numberTwo = romanToArabic(elements[2]);
            calculator.format = Format.ROME;
        } catch (IllegalArgumentException e) {
            try {
                calculator.numberOne = Integer.parseInt(elements[0]);
                calculator.numberTwo = Integer.parseInt(elements[2]);
                calculator.format = Format.ARABIC;
            } catch (NumberFormatException ex) {
                throw new InvalidFormatException();
            }
        }

        if (calculator.numberOne > 100 || calculator.numberTwo > 100) {
            throw new TooManyNumbersException();
        }

        return calculator;
    }

    private static int romanToArabic(String input){
        String romanNumeral = input.toUpperCase();
        int result = 0;

        RomanNumeral[] romanNumerals = RomanNumeral.values();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.length)) {
            RomanNumeral symbol = romanNumerals[i];
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + " cannot be converted to a Roman Numeral");
        }

        return result;
    }

    enum RomanNumeral {
        M(1000), CM(900), D(500), CD(400), C(100), XC(90), L(50),
        XL(40), X(10), IX(9), V(5), IV(4), I(1);

        private final int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum Format {
        ARABIC,
        ROME
    }

    enum Operation {
        PLUS("+"),
        MINUS("-"),
        DIVIDE("/"),
        MULTIPLY("*");

        private final String sign;

        Operation(String sign) {
            this.sign = sign;
        }

        public static Operation bySign(String sign) {
            return Arrays.stream(Operation.values())
                    .filter(op -> op.sign.equals(sign))
                    .findFirst()
                    .orElseThrow(UnknownOperationException::new);
        }
    }

    public static class UnknownOperationException extends RuntimeException {
    }

    public static class InvalidFormatException extends RuntimeException {
    }

    public static class TooManyNumbersException extends RuntimeException {
    }

    public static class NegativeRomanNumberException extends RuntimeException {
    }

    public static class InvalidNumberOfOperandsException extends RuntimeException {
    }
}

