package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.PredictableWorkLoad;
import org.sweet.jazz.core.util.JazzCoreHelper;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

import java.text.NumberFormat;

public class DefaultPredictableWorkLoad extends AbstractWork implements PredictableWorkLoad {

    private final int totalAmount;

    private int nbOk = 0;

    private int amountDone = 0;

    DefaultPredictableWorkLoad(Activity parent, final int totalAmount) {
        super(parent);

        this.totalAmount = totalAmount;

        checkFinished();
    }

    public void worked(String message) {
        ++nbOk;
        ++amountDone;

        log(getWorkMessage(message, false, null));

        checkFinished();
    }

    public void failed(String message, Throwable cause) {
        ++amountDone;

        log(getWorkMessage(message, true, cause));

        checkFinished();
    }

    public void worked(final boolean ok, String message, Throwable cause) {
        if (ok) {
            worked(message);
        } else {
            failed(message, cause);
        }
    }

    public PredictableWorkLoad synchronize() {
        return new SynchronizedPredictableWorkLoad(this);
    }

    private void checkFinished() {
        if (isFinished()) {
            log(getDoneMessage());
        }
    }

    private boolean isFinished() {
        return amountDone == totalAmount;
    }

    private String getWorkMessage(String message, final boolean failure, Throwable t) {
        Duration duration = Duration.between(start, Instant.now());
        NumberFormat numberFormat = JazzCoreHelper.createPrettyIntegerFormatter();
        String pattern = "%" + String.valueOf(totalAmount)
                .length() + "s";
        StringBuilder sb = new StringBuilder();

        sb.append("(");
        sb.append(String.format(pattern, numberFormat.format(amountDone)));
        sb.append(" / ");
        sb.append(String.format(pattern, numberFormat.format(totalAmount)));
        sb.append(", ");

        final int percentage = getPercentage();

        sb.append(String.format("%3s", numberFormat.format(percentage)));
        sb.append("% in ");
        sb.append(JazzCoreHelper.display(duration));
        sb.append(", ");

        final double multiplier = getRemainingMultiplier();
        Duration remaining = Duration.ofSeconds((long) (duration.getSeconds() * multiplier));

        sb.append(JazzCoreHelper.display(remaining));
        sb.append(" remaining");

        if (amountDone != nbOk) {
            sb.append(", ");
            sb.append(String.format(pattern, numberFormat.format(amountDone - nbOk)));
            sb.append(" failure(s)");
        }

        sb.append(") ");
        sb.append(message);

        if (failure) {
            sb.append(" #failed");

            if (t != null && t.getMessage() != null) {
                sb.append(" {");
                sb.append(t.getMessage());
                sb.append("}");
            }
        } else {
            sb.append(" #done");
        }

        return sb.toString();
    }

    private String getDoneMessage() {
        Duration duration = Duration.between(start, Instant.now());
        StringBuilder sb = new StringBuilder();

        if (isCancelled()) {
            sb.append("#cancelled");
        } else {
            sb.append("#done");
        }

        if (amountDone == 0) {
            sb.append(" (last ");
            sb.append(JazzCoreHelper.display(duration));
        } else {
            NumberFormat numberFormat = JazzCoreHelper.createPrettyIntegerFormatter();

            sb.append(" (");
            sb.append(String.format("%3s", numberFormat.format(getSuccessPercentage())));
            sb.append("% success in ");
            sb.append(JazzCoreHelper.display(duration));
            sb.append(", average for ");
            sb.append(numberFormat.format(amountDone));
            sb.append(" task(s) is ");
            sb.append(JazzCoreHelper.display(duration.dividedBy(amountDone)));
        }

        sb.append(")");

        return sb.toString();
    }

    private int getPercentage() {
        if (totalAmount != 0) {
            return amountDone * 100 / totalAmount;
        }

        return 0;
    }

    private int getSuccessPercentage() {
        if (amountDone != 0) {
            return nbOk * 100 / amountDone;
        }

        return 0;
    }

    private double getRemainingMultiplier() {
        final double remaining = totalAmount - amountDone;
        final double multiplier = remaining / amountDone;

        return multiplier;
    }
}
