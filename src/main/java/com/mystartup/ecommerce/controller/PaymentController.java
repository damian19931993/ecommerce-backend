package com.mystartup.ecommerce.controller;

import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.mystartup.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Preference> createPreference(@RequestBody Map<String, List<Map<String, Object>>> items) {
        try {
            Preference preference = paymentService.createPreference(items.get("items"));
            return ResponseEntity.ok(preference);
        } catch (MPException e) {
            e.printStackTrace();  // Agregar logging para más detalles
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
