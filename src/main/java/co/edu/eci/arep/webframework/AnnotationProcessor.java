package co.edu.eci.arep.webframework;

import co.edu.eci.arep.webframework.annotations.GetMapping;
import co.edu.eci.arep.webframework.annotations.RequestParam;
import co.edu.eci.arep.webframework.annotations.RestController;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnnotationProcessor {
    private static final Map<String, Method> routes = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();

    public static void scanForControllers(String packageName) {
        try {
            String path = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(path);
            if (resource == null) return;

            File directory = new File(resource.toURI());
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(RestController.class)) {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        controllers.put(clazz.getName(), instance);

                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(GetMapping.class)) {
                                GetMapping annotation = method.getAnnotation(GetMapping.class);
                                routes.put(annotation.value(), method);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Method getRoute(String path) {
        return routes.get(path);
    }

    public static Object getController(Class<?> clazz) {
        return controllers.get(clazz.getName());
    }

    public static String[] getParameterNames(Method method) {
        return Arrays.stream(method.getParameters())
                .map(p -> p.getAnnotation(RequestParam.class).value())
                .toArray(String[]::new);
    }
}
