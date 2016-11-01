/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.anyframe.util;

import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class to append specific library files to system class loader.
 * 
 * @author SoYon Lim
 * @author JongHoon Kim
 */
@Deprecated
public class ClasspathUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(ClasspathUtil.class);

	public static sun.misc.URLClassPath getURLClassPath(ClassLoader loader)
			throws IllegalArgumentException, IllegalAccessException {
		if (!(loader instanceof URLClassLoader)) {
			return null;
		}
		return (sun.misc.URLClassPath) getUcpField().get(loader);
	}

	@SuppressWarnings("unchecked")
	private static java.lang.reflect.Field getUcpField() {
		java.lang.reflect.Field ucpField = null;
		if (ucpField == null) {
			// Add them to the URLClassLoader's classpath
			ucpField = (java.lang.reflect.Field) AccessController
					.doPrivileged(new PrivilegedAction() {
						public Object run() {
							java.lang.reflect.Field ucp = null;

							try {
								ucp = URLClassLoader.class
										.getDeclaredField("ucp");
								ucp.setAccessible(true);

							} catch (SecurityException e) {
								logger.error(
										"Cannot access field 'ucp'. Error : {}",
										new Object[] { e.getMessage() });

							} catch (NoSuchFieldException e) {
								logger.error(
										"Cannot find field 'ucp'. Error : {}",
										new Object[] { e.getMessage() });
							}

							return ucp;
						}
					});
		}

		return ucpField;
	}
}
