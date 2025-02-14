import co.edu.eci.arep.webframework.AnnotationProcessor;
import co.edu.eci.arep.webframework.WebServer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class WebServerTest {

    @Test
    public void testStartServer() throws Exception {
        // Iniciar el servidor en un hilo separado
        Thread serverThread = new Thread(() -> WebServer.getInstance().startServer());
        serverThread.start();

        // Hacer una pausa para asegurarse de que el servidor está corriendo
        Thread.sleep(1000);

        // Intentar conectar un socket al puerto 35000
        try (Socket socket = new Socket("localhost", 35000)) {
            assertTrue(socket.isConnected(), "El servidor no está escuchando correctamente en el puerto 35000");
        } catch (IOException e) {
            fail("No se pudo conectar al servidor en el puerto 35000");
        }
    }

    @Test
    public void testMimeTypeForUnknownFile() {
        WebServer server = WebServer.getInstance();

        // Usar una ruta con una extensión no soportada
        String path = "/unknownfile.xyz";

        String mimeType = server.getMimeType(path);

        // Verificar que el tipo MIME sea "application/octet-stream"
        assertEquals("application/octet-stream", mimeType, "El tipo MIME para un archivo desconocido es incorrecto");
    }

    @Test
    public void testGetQueryParams() {
        WebServer server = WebServer.getInstance();
        Map<String, String> params = server.getQueryParams("min=10&max=20");

        assertEquals("10", params.get("min"));
        assertEquals("20", params.get("max"));
    }

    @Test
    public void testRandomNumberGeneration() throws Exception {
        AnnotationProcessor.scanForControllers("co.edu.eci.arep.webframework.controllers");

        Method method = AnnotationProcessor.getRoute("/random");
        assertNotNull(method, "No se encontró el método /random");

        Object controller = AnnotationProcessor.getController(method.getDeclaringClass());
        assertNotNull(controller, "No se encontró el controlador para /random");

        System.out.println("Ejecutando método: " + method.getName() + " en controlador " + controller.getClass().getName());

        Object result = method.invoke(controller, 10, 20);
        assertNotNull(result, "El método /random devolvió un resultado nulo");

        String response = result.toString();
        System.out.println("Respuesta del método /random: " + response);

        int randomNumber;
        try {
            randomNumber = Integer.parseInt(response.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            fail("La respuesta no contiene un número válido: " + response);
            return;
        }

        assertTrue(randomNumber >= 10 && randomNumber <= 20, "El número aleatorio no está en el rango esperado");
    }

    @Test
    public void testHandleStaticFileRequest() throws IOException {
        WebServer server = WebServer.getInstance();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        server.handleStaticFileRequest("/index.html", outputStream);
        assertTrue(outputStream.size() > 0, "El archivo estático index.html no se sirvió correctamente");

        outputStream = new ByteArrayOutputStream();
        server.handleStaticFileRequest("/nonexistent.html", outputStream);
        String response = new String(outputStream.toByteArray());
        assertTrue(response.contains("404 Not Found"), "La respuesta para archivo inexistente no es la correcta");
    }

    @Test
    public void testGetQueryParamsWithDouble() {
        WebServer server = WebServer.getInstance();
        Map<String, String> params = server.getQueryParams("price=19.99&quantity=5");

        assertEquals("19.99", params.get("price"));
        assertEquals("5", params.get("quantity"));
    }
}
