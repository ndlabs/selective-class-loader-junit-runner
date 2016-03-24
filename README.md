# selective-class-loader-junit-runner
(descriptive name is more important than short)

[![Build Status](https://travis-ci.org/ndlabs/selective-class-loader-junit-runner.svg?branch=master)](https://travis-ci.org/ndlabs/selective-class-loader-junit-runner)
[![Release](https://jitpack.io/v/ndlabs/selective-class-loader-junit-runner.svg)](https://jitpack.io/#ndlabs/selective-class-loader-junit-runner)

Some bigger modern libraries (e.g. Spring) can leverage parts of their functionality depending on
whether one or the other class is present on the classpath in the runtime.

If you ever wanted to use the same approach and wanted to test such a code, you might have faced some
difficulties doing that.

Suppose the code you wanted to test looks like this:
```java
private static boolean isFeatureClassPresent = ClassUtils.isPresent("com.somelibrary.Feature",
		FeatureConsumer.class.getClassLoader());

public Strategy chooseStrategy() {
	if (isFeatureClassPresent) {
		return new FeaturefulStrategy(new Feature());
	} else {
		return new SimpleInternalStrategy();
	}
}
```

It can be quite simple to test the path of execution that does use the "Feature" class. In most cases it 
would be just placing that optional library on the classpath. It would be needed on the compilation 
classpath anyways to be able to compile our code.

But to have a test, that wouldn't find the class on the classpath, would be much trickier. Here's when
SelectiveClassLoaderJUnitRunner will come handy.
```java
@RunWith(SelectiveClassLoaderRunner.class)
@BanClassNamesStartingWith("com.somelibrary.Feature")
public class SelectiveClassLoaderRunnerWithBannedClassTest {
	@Test
	public void testOptionalFeatureNotUsed() {
		FeatureConsumer consumer = new FeatureConsumer();
		Strategy strategy = consumer.chooseStrategy();

		Assert.assertFalse(strategy instanceof FeaturefulStrategy);
		Assert.assertTrue(strategy instanceof SimpleInternalStrategy);
	}
}
```
