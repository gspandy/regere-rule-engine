package dev.j.regere.validation;

import dev.j.regere.MalformedException;

public final class ExpressionValidationUtil {

    public static final String REGEXP = "(\\(|\\)|\\|{2}|\\&{2}|!|([\\w]+(>|<|<=|>=|==|!=)\\w+)|\\s)+";

    private ExpressionValidationUtil() {
        // Nothing
    }

    /**
     * Valid and format the expression.
     */
    public static String validAndFormat(final String booleanExpression) throws MalformedException {
        validNull(booleanExpression);
        validRegex(booleanExpression);
        validBrackets(booleanExpression);
        return format(booleanExpression);
    }

    private static void validNull(final String booleanExpression) {
        if (booleanExpression == null || booleanExpression.equals("")) {
            throw new IllegalArgumentException("Expression cannot be null or blank");
        }
    }

    private static void validRegex(String booleanExpression) throws MalformedException {
        booleanExpression = booleanExpression.replaceAll(" ", "");
        if (!booleanExpression.matches("^" + REGEXP + "$")) {
            throw new MalformedException("Expected values [ ' ' ( ) || && ! ([\\w]+(>|<|<=|>=|==)\\w+) ] but booleanExpression[" + booleanExpression + "] was not");
        }
    }

    private static void validBrackets(final String booleanExpression)
            throws MalformedException {
        int length = booleanExpression.length();
        int openBrackets = 0;
        int closeBrackets = 0;
        int lastOpenBracketsIndex = 0;
        for (int i = 0; i < length; i++) {
            char charAt = booleanExpression.charAt(i);
            switch (charAt) {
                case '(': {
                    lastOpenBracketsIndex = i;
                    openBrackets++;
                    break;
                }
                case ')': {
                    closeBrackets++;
                    if (openBrackets < closeBrackets) {
                        throw new MalformedException("Have a close bracket close without an open index[" + i + "] expression [" + booleanExpression + "]");
                    }
                    break;
                }
                default:
                    break;
            }
        }
        if (openBrackets > closeBrackets) {
            throw new MalformedException("Have an open brackets without a close brackets" + lastOpenBracketsIndex + booleanExpression);
        }
    }

    /**
     * Format the the supplied boolean expression.
     */
    private static String format(final String expression) {
        return expression.trim().replaceAll(" ", "").replaceAll("\\|\\|", "|").replaceAll("&&", "&");
    }

}
