package menu.helper;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatCurrency(double amount) {

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return currencyFormat.format(amount);
    }
}
