package loginmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import loginmvc.repository.BitacoraRepository;

@Controller
public class BitacoraController {

    @Autowired
    private BitacoraRepository bitacoraRepository;

    @GetMapping("/bitacora")
    public String bitacora(Model model) {

        model.addAttribute(
                "listaBitacora",
                bitacoraRepository.findAll());

        return "bitacora";

    }

}