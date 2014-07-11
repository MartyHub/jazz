package org.sweet.jazz.core.util;

import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public final class JazzCoreHelper {

    public static String display(Duration duration) {
        StringBuilder sb = new StringBuilder();

        final long hours = duration.toHours();
        append(sb, hours, 2);
        sb.append(':');

        duration = duration.minus(hours, ChronoUnit.HOURS);

        final long minutes = duration.toMinutes();
        append(sb, minutes, 2);
        sb.append(':');

        duration = duration.minus(minutes, ChronoUnit.MINUTES);

        final long seconds = duration.getSeconds();
        append(sb, seconds, 2);

        return sb.toString();
    }

    public static void append(StringBuilder sb, final long value, final int minWidth) {
        String s = String.valueOf(value);

        for (int i = minWidth - s.length(); i > 0; --i) {
            sb.append('0');
        }

        sb.append(s);
    }

    public static NumberFormat createPrettyIntegerFormatter() {
        DecimalFormat result = new DecimalFormat("#,##0");

        fixGroupingSeparator(result);

        return result;
    }

    public static DecimalFormat fixGroupingSeparator(DecimalFormat df) {
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');

        df.setDecimalFormatSymbols(symbols);

        return df;
    }

    public static String repeat(final char c, final int count) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < count; ++i) {
            sb.append(c);
        }

        return sb.toString();
    }

    private JazzCoreHelper() {
    }
}
