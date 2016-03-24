package com.ndlabs.test.util.classloading.example;

import com.ndlabs.test.util.classloading.optional.TestFeature;

public class FeaturefulStrategy implements Strategy {

	private TestFeature testFeature;

	public FeaturefulStrategy(TestFeature testFeature) {
		//implement the strategy using the feature if appropriate lib is on the classpath
		this.testFeature = testFeature;
	}

	public TestFeature getTestFeature() {
		return testFeature;
	}
}
