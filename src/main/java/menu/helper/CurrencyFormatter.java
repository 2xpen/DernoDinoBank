package menu.helper;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatCurrency(double amount) {

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return currencyFormat.format(amount);
    }

    public static String formatCurrencyForCSV(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String formatted = currencyFormat.format(amount);
        return formatted.replaceAll("\\p{Sc}", "").trim(); // Währungssymbol entfernen
    }
}
