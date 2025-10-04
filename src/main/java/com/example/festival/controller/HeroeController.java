package com.example.festival.controller;

import com.example.festival.entity.Heroe;
import com.example.festival.repository.HeroeRepository;
import com.example.festival.repository.RangoRepository;
import jakarta.validation.Valid;
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
    final RangoRepository rangoRepository;

    public HeroeController(HeroeRepository heroeRepository, RangoRepository rangoRepository) {
        this.heroeRepository = heroeRepository;
        this.rangoRepository = rangoRepository;
    }

    @GetMapping()
    public String listaHeroes(Model model) {

        model.addAttribute("listaHeroes", heroeRepository.findAll());
        return "heroes";
    }


    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarVistaNuevoHeroe(Model model, @ModelAttribute Heroe heroe) {

        model.addAttribute("heroe", heroe);

        model.addAttribute("listaRangos", rangoRepository.findAll());
        return "nuevoHeroe";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarHeroe(Model model, @Valid @ModelAttribute Heroe heroe, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaRangos", rangoRepository.findAll());
            return "nuevoHeroe";
        }

        heroeRepository.save(heroe);
        redirectAttributes.addFlashAttribute("msg", "Nuevo héroe creado correctamente");

        return "redirect:/heroes";
    }

}
