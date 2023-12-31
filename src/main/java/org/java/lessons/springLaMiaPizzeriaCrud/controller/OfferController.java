package org.java.lessons.springLaMiaPizzeriaCrud.controller;

import jakarta.validation.Valid;
import org.java.lessons.springLaMiaPizzeriaCrud.model.Offer;
import org.java.lessons.springLaMiaPizzeriaCrud.model.Pizza;
import org.java.lessons.springLaMiaPizzeriaCrud.repository.OfferRepository;
import org.java.lessons.springLaMiaPizzeriaCrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/offers")
public class OfferController {
    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    OfferRepository offerRepository;

    @GetMapping("/create")
    public String create(@RequestParam Integer pizzaId, Model model) {
        Pizza pizza = pizzaRepository.findById(pizzaId).orElseThrow(()->new RuntimeException("La pizza con id " + pizzaId + " non trovato!"));
        Offer offer = new Offer();
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusMonths(1));
        offer.setPizza(pizza);
        model.addAttribute("offer", offer);
        return "offers/form";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("offer") Offer formOffer, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        // valido se i dati sono corretti
        if (bindingResult.hasErrors()) {
            return "offers/form";
        } else {
            // se i dati sono corretti salvo il libro su database
            Offer savedOffer = offerRepository.save(formOffer);
            redirectAttributes.addFlashAttribute("messageOffer", "L'offerta: " + savedOffer.getTitle() + " è stata " +
                    "creata " + "con successo!");
        // redirect al dettaglio della pizza
        return "redirect:/pizzas/show/" + savedOffer.getPizza().getId();
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Offer offer = offerRepository.findById(id).orElseThrow(()->new RuntimeException("L'ordine con id " + id +
                " non trovato!"));
        model.addAttribute("offer", offer);
        return "offers/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("offer") Offer formOffer, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "offers/form";
        } else {
            Offer savedOffer = offerRepository.save(formOffer);
            redirectAttributes.addFlashAttribute("messageOffer", "L'offerta: " + savedOffer.getTitle() + " è stata " +
                    "modificata con successo!");
            return "redirect:/pizzas/show/" + savedOffer.getPizza().getId();
        }
    }
}
