package co.edu.eci.arep.webframework;

public class WebFramework {
    public static void main(String[] args) {
        AnnotationProcessor.scanForControllers("co.edu.eci.arep.webframework.controllers");
        WebServer.getInstance().startServer();
    }
}
