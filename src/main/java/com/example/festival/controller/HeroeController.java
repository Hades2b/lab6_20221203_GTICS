package com.example.festival.controller;

import com.example.festival.entity.Heroe;
import com.example.festival.repository.HeroeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/heroes")
public class HeroeController {

    final HeroeRepository heroeRepository;

    public HeroeController(HeroeRepository heroeRepository) {
        this.heroeRepository = heroeRepository;
    }

    @GetMapping("/")
    public String listaHeroes(Model model) {

        model.addAttribute("listaHeroes", heroeRepository.findAll());
        return "heroes";
    }


    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarVistaNuevoHeroe(Model model, @ModelAttribute Heroe heroe) {

        model.addAttribute("heroe", heroe);

        return "nuevoHeroe";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarHeroe(Model model, @ModelAttribute Heroe heroe, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "nuevoHeroe";
        }

        heroeRepository.save(heroe);
        redirectAttributes.addFlashAttribute("msg", "Nuevo héroe creado correctamente");

        return "redirect:/heroes/";
    }

}
