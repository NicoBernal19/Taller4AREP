import co.edu.eci.arep.webframework.AnnotationProcessor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class AnnotationProcessorTest {

    @Test
    public void testScanForControllers() {
        AnnotationProcessor.scanForControllers("co.edu.eci.arep.webframework.controllers");
        assertFalse(AnnotationProcessor.getRoute("/hello") == null, "La ruta '/hello' no se registró correctamente");
    }

    @Test
    public void testGetParameterNames() throws Exception {
        AnnotationProcessor.scanForControllers("co.edu.eci.arep.webframework.controllers");

        Method method = AnnotationProcessor.getRoute("/hello");
        assertNotNull(method, "No se encontró el método /hello");

        String[] paramNames = AnnotationProcessor.getParameterNames(method);

        assertNotNull(paramNames, "El array de parámetros es nulo");
        assertTrue(paramNames.length > 0, "No se detectaron parámetros en el método");

        assertArrayEquals(new String[]{"name"}, paramNames, "Los parámetros del método /hello son incorrectos");
    }

    @Test
    public void testScanForControllersWithValidController() {
        AnnotationProcessor.scanForControllers("co.edu.eci.arep.webframework.controllers");
        Method method = AnnotationProcessor.getRoute("/greeting");
        assertNotNull(method, "La ruta '/greeting' se registró correctamente");
    }
}
