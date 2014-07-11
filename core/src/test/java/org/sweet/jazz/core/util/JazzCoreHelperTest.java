package org.sweet.jazz.core.util;

import org.junit.Test;
import org.threeten.bp.Duration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JazzCoreHelperTest {

    @Test
    public void test_display() {
        assertThat(JazzCoreHelper.display(Duration.ofHours(1)
                .plusMinutes(2)
                .plusSeconds(3)), is(equalTo("01:02:03")));

        assertThat(JazzCoreHelper.display(Duration.ofHours(100)
                .plusMinutes(59)
                .plusSeconds(59)), is(equalTo("100:59:59")));
    }
}
