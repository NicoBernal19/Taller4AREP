package co.edu.eci.arep.webframework.controllers;

import co.edu.eci.arep.webframework.annotations.GetMapping;
import co.edu.eci.arep.webframework.annotations.RequestParam;
import co.edu.eci.arep.webframework.annotations.RestController;

@RestController
public class GreetingController {
    @GetMapping("/greeting")
    public String greeting() {
        return "Hola! Bienvenido a mi servidor Web hecho en Java.";
    }
    @GetMapping("/later")
    public static String bye() {
        return "Nos vemos luego!";
    }
    @GetMapping("/hello")
    public static String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }
}
