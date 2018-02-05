/**
 * 
 */
package idv.mint.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.Entity;

/**
 * @author WuJerry
 *
 */
public class ScanningPackageUtils {

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {

		List<Class<?>> allClasses = loadClasses(packageName);

		return allClasses;
	}

	public static List<Class<?>> getClassesByClassAnnotation(String packageName, Class<?> classAnnotation) throws ClassNotFoundException, IOException {

		List<Class<?>> clazzList = new ArrayList<Class<?>>();

		List<Class<?>> allClasses = loadClasses(packageName);

		for (Class<?> clazz : allClasses) {
			Entity entityAnnotation = clazz.getAnnotation(Entity.class);
			if (entityAnnotation != null) {
				clazzList.add(clazz);
			}
		}
		return clazzList;
	}

	private static List<Class<?>> loadClasses(String packageName) throws IOException, ClassNotFoundException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		assert classLoader != null;

		String path = packageName.replace('.', '/');

		Enumeration<URL> resources = classLoader.getResources(path);

		List<File> dirs = new ArrayList<>();

		while (resources.hasMoreElements()) {

			URL resource = resources.nextElement();

			dirs.add(new File(resource.getFile()));

		}

		List<Class<?>> classes = new ArrayList<>();

		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}

		return classes;
	}

	/**
	 * 
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 *
	 * 
	 * @param directory
	 *            The base directory
	 * 
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * 
	 * @return The classes
	 * 
	 * @throws ClassNotFoundException
	 */

	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {

		List<Class<?>> classes = new ArrayList<>();

		if (!directory.exists()) {
			return classes;
		}

		File[] files = directory.listFiles();

		for (File file : files) {

			if (file.isDirectory()) {

				assert !file.getName().contains(".");

				classes.addAll(findClasses(file, packageName + "." + file.getName()));

			} else if (file.getName().endsWith(".class")) {

				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}

		return classes;

	}
}
