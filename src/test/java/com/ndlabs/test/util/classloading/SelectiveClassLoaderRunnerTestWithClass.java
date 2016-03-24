package com.ndlabs.test.util.classloading;

import com.ndlabs.test.util.classloading.example.FeaturefulStrategy;
import com.ndlabs.test.util.classloading.example.SimpleInternalStrategy;
import com.ndlabs.test.util.classloading.example.Strategy;
import com.ndlabs.test.util.classloading.example.FeatureConsumer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SelectiveClassLoaderRunner.class)
public class SelectiveClassLoaderRunnerTestWithClass {

	@Test
	public void testOptionalFeatureUsed() {
		FeatureConsumer consumer = new FeatureConsumer();
		Strategy strategy = consumer.chooseStrategy();

		Assert.assertFalse(strategy instanceof SimpleInternalStrategy);
		Assert.assertTrue(strategy instanceof FeaturefulStrategy);
		Assert.assertNotNull(((FeaturefulStrategy) strategy).getFeature());
	}
}