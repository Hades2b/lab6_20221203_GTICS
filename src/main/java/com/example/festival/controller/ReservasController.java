package com.example.festival.controller;

import com.example.festival.entity.Mesa;
import com.example.festival.entity.Reserva;
import com.example.festival.entity.Usuario;
import com.example.festival.repository.MesaRepository;
import com.example.festival.repository.ReservaRepository;
import com.example.festival.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/reservas")
public class ReservasController {

    final UsuarioRepository usuarioRepository;
    final MesaRepository mesaRepository;
    final ReservaRepository reservaRepository;

    public ReservasController(UsuarioRepository usuarioRepository, MesaRepository mesaRepository, ReservaRepository reservaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.mesaRepository = mesaRepository;
        this.reservaRepository = reservaRepository;
    }

    @GetMapping("/mesas")
    public String reservasDeMesas(Model model) {

        List<Mesa> mesas = mesaRepository.findAll();

        int disponibles = 0, ocupadas = 0;
        for (Mesa mesa : mesas) {
            if (mesa.getDisponible() && mesa.getReservas().size() < mesa.getCapacidad()) {
                disponibles++;
            } else {
                ocupadas++;
            }
        }
        model.addAttribute("disponibles", disponibles);
        model.addAttribute("ocupadas", ocupadas);

        model.addAttribute("listaMesas", mesas);

        return "reservas/verMesas";

    }

    @PostMapping("/mesas/{idMesa}/reservar/")
    public String reservarMesa(@PathVariable Integer idMesa, Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());

        Reserva mesaReservada = reservaRepository.findByMesa_IdAndUsuario_Id(idMesa,usuario.getId());
        if (mesaReservada != null) {
            redirectAttributes.addFlashAttribute("msg", "Usted ya reservo la mesa número: " + mesaReservada.getMesa().getNumero());
            return "redirect:/reservas/mesas";
        }

        Optional<Mesa> mesa = mesaRepository.findById(idMesa);
        if (mesa.isPresent()) {
            Mesa mesaX = mesa.get();
            if (mesaX.getCapacidad()==mesaX.getReservas().size() || !mesaX.getDisponible()) {
                redirectAttributes.addFlashAttribute("msg", "La mesa esta a su máxima capacidad: " + mesaX.getCapacidad());
                return "redirect:/reservas/mesas";
            } else {
                Reserva nuevaReservaMesa = new Reserva(usuario,mesa.get(), LocalDateTime.now());
                reservaRepository.save(nuevaReservaMesa);
                redirectAttributes.addFlashAttribute("msg", "Se reservo la mesa con exito");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg", "La mesa no existe");
        }

        return "redirect:/reservas/mesas";
    }


    @GetMapping("/mesas/{idMesa}")
    @PreAuthorize("hasRole('ADMIN')")
    public String administrarMesa(Model model, @PathVariable Integer idMesa, RedirectAttributes redirectAttributes) {

        Optional<Mesa> mesa = mesaRepository.findById(idMesa);

        if (mesa.isPresent()) {
            model.addAttribute("mesa", mesa.get());
        } else {
            redirectAttributes.addFlashAttribute("msg", "La mesa no existe");
            return "redirect:/reservas/mesas";
        }

        return "reservas/mesaConfig";

    }

    @PostMapping("/mesas/{idMesa}/actualizar")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizarMesa(Model model, @PathVariable Integer idMesa, @ModelAttribute Mesa mesa, RedirectAttributes redirectAttributes) {

        Optional<Mesa> mesaX = mesaRepository.findById(idMesa);
        if (mesaX.isPresent()) {
            Mesa mesa1 = mesaX.get();
            mesa1.setCapacidad(mesa.getCapacidad());
            mesa1.setNumero(mesa.getNumero());
            mesa1.setDisponible(mesa.getDisponible());
            mesaRepository.save(mesa1);
            redirectAttributes.addFlashAttribute("msg", "Mesa actualizada con exito");
        } else {
            redirectAttributes.addFlashAttribute("msg", "La mesa no existe");
        }

        return "redirect:/reservas/mesas/"+idMesa;
    }


    @PostMapping("/{idReserva}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarReserva(Model model, @PathVariable Integer idReserva, RedirectAttributes redirectAttributes) {

        Optional<Reserva> reserva = reservaRepository.findById(idReserva);

        if (reserva.isPresent()) {
            reservaRepository.delete(reserva.get());
        } else {
            redirectAttributes.addFlashAttribute("msg", "La reserva no existe");
        }

        return "redirect:/reservas/mesas";
    }


}
