package com.ndlabs.test.util.classloading;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SelectiveClassLoaderRunner extends Runner {
	private static final String[] EMPTY_STRING_ARRAY = {};
	private static final String[] DEFAULT_SHARED_CLASS_NAME_PATTERNS = {"org.junit.runner."};

	private final Runner delegateRunner;
	private final SelectiveClassLoader instrumentedClassLoader;

	public SelectiveClassLoaderRunner(Class<?> testClass) throws InitializationError {

		Class<? extends Runner> delegateRunnerClass = JUnit4.class;
		DelegateRunWith annotation = testClass.getAnnotation(DelegateRunWith.class);
		if(annotation != null) {
			delegateRunnerClass = annotation.value();
		}

		String[] bannedClassNamePatterns = EMPTY_STRING_ARRAY;
		BanClassNamesStartingWith ban = testClass.getAnnotation(BanClassNamesStartingWith.class);
		if (ban != null) {
			bannedClassNamePatterns = ban.value();
		}

		Set<String> banned = new HashSet<>(Arrays.asList(bannedClassNamePatterns));
		Set<String> shared = new HashSet<>(Arrays.asList(DEFAULT_SHARED_CLASS_NAME_PATTERNS));
		instrumentedClassLoader = new SelectiveClassLoader(banned, shared);

		try {
			Class<?> customLoadedDelegateRunnerClass = instrumentedClassLoader.loadClass(delegateRunnerClass.getName());
			Class<?> customLoadedTestClass = instrumentedClassLoader.loadClass(testClass.getName());
			delegateRunner = (Runner) customLoadedDelegateRunnerClass.getConstructor(Class.class).newInstance(customLoadedTestClass);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new InitializationError(e);
		}
	}

	@Override
	public Description getDescription() {
		return delegateRunner.getDescription();
	}

	@Override
	public void run(RunNotifier notifier) {
		ClassLoader pcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(instrumentedClassLoader);
		delegateRunner.run(notifier);
		Thread.currentThread().setContextClassLoader(pcl);
	}
}
