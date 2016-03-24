package com.ndlabs.test.util.classloading.example;

import com.ndlabs.test.util.classloading.optional.Feature;

public class FeatureConsumer {

	private static boolean isFeatureClassPresent = ClassUtils.isPresent("com.ndlabs.test.util.classloading.optional.Feature",
			FeatureConsumer.class.getClassLoader());

	public Strategy chooseStrategy() {
		if (isFeatureClassPresent) {
			return new FeaturefulStrategy(new Feature());
		} else {
			return new SimpleInternalStrategy();
		}
	}

}
