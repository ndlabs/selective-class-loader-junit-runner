package com.ndlabs.test.util.classloading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;
import java.util.*;

public class SelectiveClassLoader extends URLClassLoader {
	private static final Logger log = LoggerFactory.getLogger(SelectiveClassLoader.class);

	private final Set<String> bannedClassNamePatterns = new HashSet<>();
	private final Set<String> sharedClassNamePatterns = new HashSet<>();
	private final Map<String, Class<?>> asd = Collections.synchronizedMap(new HashMap<String, Class<?>>());
	private final ClassLoader parent;
	private final ClassLoader extClassLoader;

	public SelectiveClassLoader(Set<String> bannedClassNamePatterns) {
		this(bannedClassNamePatterns, null);
	}

	public SelectiveClassLoader(Set<String> bannedClassNamePatterns, Set<String> sharedClassNamePatterns) {
		super(((URLClassLoader) getSystemClassLoader()).getURLs());

		ClassLoader p = getParent();
		if (p == null) {
			p = getSystemClassLoader();
		}
		this.parent = p;
		this.extClassLoader = calcExtClassLoader();

		if (bannedClassNamePatterns != null) {
			this.bannedClassNamePatterns.addAll(bannedClassNamePatterns);
		}

		if (sharedClassNamePatterns != null) {
			this.sharedClassNamePatterns.addAll(sharedClassNamePatterns);
		}
	}

	private ClassLoader calcExtClassLoader() {
		ClassLoader res = String.class.getClassLoader();
		if (res == null) {
			res = getSystemClassLoader();
			while (res.getParent() != null) {
				res = res.getParent();
			}
		}
		return res;
	}

	private boolean matches(String name, Iterable<String> patterns) {
		for (String bannedPattern : patterns) {
			if (name.startsWith(bannedPattern)) {
				return true;
			}
		}
		return false;
	}

	private Class<?> loadSharedClass(String name) {
		try {
			return Class.forName(name, false, extClassLoader);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> res = findLoadedClass(name);
		if (res != null) {
			log.debug("Found in cache [{}]", name);
		} else {
			res = loadSharedClass(name);
			if (res != null) {
				log.debug("Loaded from ext classloader [{}]", name);
			} else if (matches(name, bannedClassNamePatterns)) {
				log.info("Banned loading [{}]", name);
				throw new ClassNotFoundException();
			} else if (matches(name, sharedClassNamePatterns)) {
				log.debug("Loading from parent classloader [{}]", name);
				res = Class.forName(name, false, parent);
			} else {
				log.debug("Loading from current classloader [{}]", name);
				res = findClass(name);
			}
		}
		return res;
	}
}