package com.ndlabs.test.util.classloading.example;

import com.ndlabs.test.util.classloading.optional.TestFeature;

public class TestFeatureConsumer {

	private static boolean isFeatureClassPresent = ClassUtils.isPresent("com.ndlabs.test.util.classloading.optional.TestFeature",
			TestFeatureConsumer.class.getClassLoader());

	public Strategy chooseStrategy() {
		if (isFeatureClassPresent) {
			return new FeaturefulStrategy(new TestFeature());
		} else {
			return new SimpleInternalStrategy();
		}
	}

}
