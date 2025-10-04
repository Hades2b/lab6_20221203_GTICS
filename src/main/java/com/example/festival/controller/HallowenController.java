package com.example.festival.controller;

import com.example.festival.dto.NuevaAsignacionNum;
import com.example.festival.entity.AsignacionCancion;
import com.example.festival.entity.Cancion;
import com.example.festival.entity.NumeroCasa;
import com.example.festival.entity.Usuario;
import com.example.festival.repository.NumeroCasaRepository;
import com.example.festival.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/halloween")
public class HallowenController {

    final UsuarioRepository usuarioRepository;
    final NumeroCasaRepository numeroCasaRepository;

    public HallowenController(UsuarioRepository usuarioRepository, NumeroCasaRepository numeroCasaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.numeroCasaRepository = numeroCasaRepository;
    }

    @GetMapping
    public String verNumAsig(Model model, Authentication auth, HttpSession session) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());

        NumeroCasa numAsignado = numeroCasaRepository.findByUsuario_Id(usuario.getId());

        if (numAsignado != null) {

            if (numAsignado.getNumeroObjetivo()==null) {
                model.addAttribute("solicitada", true);
            } else {
                model.addAttribute("solicitada", true);
                model.addAttribute("numAsignado", numAsignado);

                if (session.getAttribute("numIntentos") == null) {
                    session.setAttribute("numIntentos", 0);
                }
                model.addAttribute("numIntentos", session.getAttribute("numIntentos"));
                if (session.getAttribute("numProgreso") == null) {
                    session.setAttribute("numProgreso", 0);
                }
                model.addAttribute("numProgreso", session.getAttribute("numProgreso"));
            }

        } else {
            model.addAttribute("solicitada", false);
        }

        return "/hallo/verNumAsig";
    }


    @PostMapping("/solicitar")
    public String solicitarAsignacionCancion(Model model, Authentication auth) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());
        NumeroCasa numAsig = numeroCasaRepository.findByUsuario_Id(usuario.getId());

        if (numAsig != null) {
            if (numAsig.getAdivinado() != null && !numAsig.getAdivinado()) {
                return "redirect:/halloween";
            } else {
                numeroCasaRepository.deleteById(numAsig.getId());
            }
        }

        NumeroCasa nuevaAsignacion = new NumeroCasa(usuario);
        numeroCasaRepository.save(nuevaAsignacion);

        return "redirect:/halloween";
    }

    @PostMapping("/adivinar")
    public String adivinarCancion(@RequestParam int adivinar, Model model, Authentication auth, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());

        NumeroCasa numAsig = numeroCasaRepository.findByUsuario_Id(usuario.getId());

        if (numAsig != null && numAsig.getNumeroObjetivo() != null) {
            Integer numObjetivo = numAsig.getNumeroObjetivo();
            Integer numIntentos = (Integer) session.getAttribute("numIntentos");
            Integer numProgreso = (Integer) session.getAttribute("numProgreso");


            if (numObjetivo != null && numIntentos != null && numProgreso != null) {
                if (numObjetivo== numProgreso+adivinar) {
                    numAsig.setAdivinado(true);
                    numAsig.setIntentos(numIntentos+1);
                    numeroCasaRepository.save(numAsig);
                    redirectAttributes.addFlashAttribute("msg", "Haz adivinado el numero de pasos en "+(numIntentos+1)+" intentos");
                    numProgreso += adivinar;

                } else if (numObjetivo > numProgreso+adivinar) {
                    redirectAttributes.addFlashAttribute("msg", "Te faltan pasos!");
                    numProgreso += adivinar;

                } else if (numObjetivo < numProgreso+adivinar) {
                    redirectAttributes.addFlashAttribute("msg", "Te has pasado del numero objetivo!");

                }
                numIntentos = numIntentos +1;
            }


            session.setAttribute("numProgreso", numProgreso);
            session.setAttribute("numIntentos", numIntentos);
            session.setAttribute("asignacion", numAsig);

        } else return "redirect:/halloween";

        return "redirect:/halloween";
    }


    @GetMapping("/listaAsignaciones")
    @PreAuthorize("hasRole('ADMIN')")
    public String listaAsignacionesNum(Model model) {
        model.addAttribute("asignaciones", numeroCasaRepository.findAll());
        return "/hallo/listaAsignacionesNum";
    }

    @GetMapping("/nuevaAsignacion")
    @PreAuthorize("hasRole('ADMIN')")
    public String crearAsignacionNueva(Model model) {

        model.addAttribute("nuevaAsignacionNumDto", new NuevaAsignacionNum());
        model.addAttribute("listaUsuariosSolicitantes", numeroCasaRepository.findByNumeroObjetivoNull());

        return "/hallo/nuevaAsignacionNum";
    }

    @PostMapping("/asignar")
    @PreAuthorize("hasRole('ADMIN')")
    public String creaOeditarAsignacion(Model model, Authentication auth, @ModelAttribute NuevaAsignacionNum nuevaAsignacionNumDto, RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuario = usuarioRepository.findById(nuevaAsignacionNumDto.getIdUsuario());

        if (usuario.isPresent()) {
            NumeroCasa asigOrig = numeroCasaRepository.findByUsuario_Id(usuario.get().getId());
            NumeroCasa nuevaAsignacion = new NumeroCasa(asigOrig.getId(),usuario.get(), nuevaAsignacionNumDto.getNumObjetivo());
            numeroCasaRepository.save(nuevaAsignacion);
            redirectAttributes.addFlashAttribute("msg", "Asignación creada correctamente");

        } else {
            model.addAttribute("nuevaAsignacioNumDto", nuevaAsignacionNumDto);
            model.addAttribute("listaUsuariosSolicitantes", numeroCasaRepository.findByNumeroObjetivoNull());
            return "/hallo/nuevaAsignacionNum";
        }

        return "redirect:/halloween/listaAsignaciones";
    }

    @GetMapping("/ranking")
    @PreAuthorize("isAuthenticated()")
    public String mostrarRankingNum(Model model) {
        model.addAttribute("listaRanking", numeroCasaRepository.findByAdivinadoTrueOrderByIntentosAsc());
        return "/hallo/rankingNum";
    }

}
