package org.sweet.jazz.core.log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sweet.jazz.core.test.WeldJUnit4Runner;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(WeldJUnit4Runner.class)
public class ActivityProducerTest {

    @Inject
    private Activity activity;

    @Test
    public void test_create_activity() {
        assertThat(activity, is(notNullValue()));

        activity.log("test");

        assertThat(activity.getName(), is(equalTo(getClass().getSimpleName())));
    }
}
