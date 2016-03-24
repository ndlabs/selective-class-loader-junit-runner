package com.ndlabs.test.util.classloading;

import com.ndlabs.test.util.classloading.example.FeaturefulStrategy;
import com.ndlabs.test.util.classloading.example.SimpleInternalStrategy;
import com.ndlabs.test.util.classloading.example.Strategy;
import com.ndlabs.test.util.classloading.example.TestFeatureConsumer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SelectiveClassLoaderRunner.class)
@BanClassNamesStartingWith("com.ndlabs.test.util.classloading.optional.TestFeature")
public class SelectiveClassLoaderRunnerTestWithBannedClass {

	@Test
	public void testOptionalFeatureNotUsed() {
		TestFeatureConsumer consumer = new TestFeatureConsumer();
		Strategy strategy = consumer.chooseStrategy();

		Assert.assertFalse(strategy instanceof FeaturefulStrategy);
		Assert.assertTrue(strategy instanceof SimpleInternalStrategy);
	}
}