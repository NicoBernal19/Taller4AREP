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
    public static String random(@RequestParam(value = "min") int min, @RequestParam(value = "max") int max) {
        int randomNum = (int) (Math.random() * (max - min + 1) + min);
        return "El número al azar que te tocó es: " + randomNum;
    }
}
