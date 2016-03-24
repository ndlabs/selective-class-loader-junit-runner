package com.ndlabs.test.util.classloading;

import com.ndlabs.test.util.classloading.example.FeaturefulStrategy;
import com.ndlabs.test.util.classloading.example.SimpleInternalStrategy;
import com.ndlabs.test.util.classloading.example.Strategy;
import com.ndlabs.test.util.classloading.example.FeatureConsumer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SelectiveClassLoaderRunner.class)
@BanClassNamesStartingWith("com.ndlabs.test.util.classloading.optional.Feature")
public class SelectiveClassLoaderRunnerWithBannedClassTest {

	@Test
	public void testOptionalFeatureNotUsed() {
		FeatureConsumer consumer = new FeatureConsumer();
		Strategy strategy = consumer.chooseStrategy();

		Assert.assertFalse(strategy instanceof FeaturefulStrategy);
		Assert.assertTrue(strategy instanceof SimpleInternalStrategy);
	}
}