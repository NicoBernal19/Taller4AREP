package co.edu.eci.arep.webframework.controllers;

import co.edu.eci.arep.webframework.annotations.GetMapping;
import co.edu.eci.arep.webframework.annotations.RequestParam;
import co.edu.eci.arep.webframework.annotations.RestController;

@RestController
public class MathController {
    @GetMapping("/pi")
    public static String pi() {
        return Double.toString(Math.PI);
    }
    @GetMapping("/e")
    public static String e(){
        return Double.toString(Math.E);
    }
    @GetMapping("/random")
    public static String random(@RequestParam(value = "min") double min, @RequestParam(value = "max") double max) {
        return "El numero al azar que te toco es: " + (Math.random() * (max - min) + min);
    }
}
