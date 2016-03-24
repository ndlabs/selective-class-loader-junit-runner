package com.ndlabs.test.util.classloading.example;

import com.ndlabs.test.util.classloading.optional.Feature;

public class FeaturefulStrategy implements Strategy {

	private Feature feature;

	public FeaturefulStrategy(Feature feature) {
		//implement the strategy using the feature if appropriate lib is on the classpath
		this.feature = feature;
	}

	public Feature getFeature() {
		return feature;
	}
}
