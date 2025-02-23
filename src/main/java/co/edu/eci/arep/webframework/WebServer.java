package co.edu.eci.arep.webframework;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WebServer {
    private static final int PORT = 35000;
    private static final String RESOURCE_PATH = "src/main/resources";
    private static WebServer instance;
    private ServerSocket serverSocket;
    private boolean running = true;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);


    private WebServer() {}

    public static WebServer getInstance() {
        if (instance == null) {
            instance = new WebServer();
        }
        return instance;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en http://localhost:" + PORT);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nApagando servidor...");
                stopServer();
            }));

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (!running) {
                        break;
                    }
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        threadPool.shutdown();

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Servidor detenido correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2) return;

            String method = tokens[0];
            String path = tokens[1];

            if (!handleServiceRequest(method, path, out)) {
                handleStaticFileRequest(path, out);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean handleServiceRequest(String method, String path, OutputStream out) throws IOException {
        String[] parts = path.split("\\?");
        String routePath = parts[0];
        String query = parts.length > 1 ? parts[1] : null;

        Method routeMethod = AnnotationProcessor.getRoute(routePath);
        if (routeMethod != null) {
            try {
                Object controller = AnnotationProcessor.getController(routeMethod.getDeclaringClass());

                Object result;
                if (query != null) {
                    Map<String, String> queryParams = getQueryParams(query);

                    Class<?>[] paramTypes = routeMethod.getParameterTypes();
                    Object[] args = new Object[paramTypes.length];

                    String[] paramNames = AnnotationProcessor.getParameterNames(routeMethod);

                    for (int i = 0; i < paramTypes.length; i++) {
                        String paramName = paramNames[i];
                        String paramValue = queryParams.get(paramName);

                        if (paramValue != null) {
                            if (paramTypes[i] == int.class) {
                                args[i] = Integer.parseInt(paramValue);
                            } else if (paramTypes[i] == double.class) {
                                args[i] = Double.parseDouble(paramValue);
                            } else {
                                args[i] = paramValue;
                            }
                        } else {
                            if (paramTypes[i] == int.class) {
                                args[i] = 0;
                            } else if (paramTypes[i] == double.class) {
                                args[i] = 0.0;
                            } else {
                                args[i] = "";
                            }
                        }
                    }

                    result = routeMethod.invoke(controller, args);
                } else {
                    result = routeMethod.invoke(controller);
                }

                sendResponse(out, "200 OK", "text/plain", ((String) result).getBytes());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(out, "500 Internal Server Error", "text/plain", "Error en el servidor".getBytes());
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }

    public void handleStaticFileRequest(String path, OutputStream out) throws IOException {
        if (path.equals("/")) {
            path = "/index.html";
        }

        File file = new File(RESOURCE_PATH + path);
        if (file.exists() && !file.isDirectory()) {
            String mimeType = getMimeType(path);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            sendResponse(out, "200 OK", mimeType, fileContent);
        } else {
            sendResponse(out, "404 Not Found", "text/plain", "Archivo no encontrado".getBytes());
        }
    }

    public String getMimeType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
    }

    private void sendResponse(OutputStream out, String status, String contentType, byte[] content) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 " + status);
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + content.length);
        writer.println();
        writer.flush();
        out.write(content);
        out.flush();
    }
}
