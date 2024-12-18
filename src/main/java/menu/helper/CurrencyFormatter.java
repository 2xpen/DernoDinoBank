package menu.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatCurrency(double amount) {

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return currencyFormat.format(amount);
    }

    public static String formatCurrencyForCSV(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return decimalFormat.format(amount);
    }
}