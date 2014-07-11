package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.UnknownWorkLoad;
import org.sweet.jazz.core.util.JazzCoreHelper;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

public class DefaultUnknownWorkLoad extends AbstractWork implements UnknownWorkLoad {

    DefaultUnknownWorkLoad(Activity parent) {
        super(parent);
    }

    public void done() {
        Duration duration = Duration.between(start, Instant.now());
        StringBuilder sb = new StringBuilder();

        if (isCancelled()) {
            sb.append("#cancelled");
        } else {
            sb.append("#done");
        }

        sb.append(" (last ");
        sb.append(JazzCoreHelper.display(duration));
        sb.append(")");

        log(sb.toString());
    }
}
