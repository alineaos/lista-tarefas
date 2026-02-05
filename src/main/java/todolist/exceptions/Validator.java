package todolist.exceptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static int validateNumber(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new BusinessException("'" + input + "' Não é um número inteiro.");
        }
    }

    public static LocalDate validateStringToDate(String input) {
        try {
            return LocalDate.parse(input, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new BusinessException("'" + input + "' Não é uma data válida.");
        }
    }

    public static void validateNotPastDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException("A data não pode estar no passado.");
        }
    }


    public static String validateDateToString(LocalDate input) {
        try {
            return input.format(dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new BusinessException("'" + input + "' Não é uma data válida.");
        }
    }

    public static void validateCategoryName(String input){
        if (input.isBlank()){
            throw new BusinessException("O nome da categoria não pode estar em branco.");
        }
    }
}
