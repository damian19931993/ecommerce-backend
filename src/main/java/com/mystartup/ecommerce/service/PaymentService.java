package com.mystartup.ecommerce.service;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    public Preference createPreference(List<Map<String, Object>> cartItems) throws MPException {
        List<PreferenceItemRequest> items = new ArrayList<>();
        for (Map<String, Object> item : cartItems) {
            PreferenceItemRequest preferenceItemRequest = PreferenceItemRequest.builder()
                    .title((String) item.get("name"))
                    .quantity(((Number) item.get("quantity")).intValue())
                    .unitPrice(new BigDecimal(((Number) item.get("price")).doubleValue()))
                    .build();
            items.add(preferenceItemRequest);
        }

        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .email("test_user@test.com") // Este email puede ser dinámico si se pasa en la solicitud
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .payer(payer)
                .build();

        PreferenceClient client = new PreferenceClient();
        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .accessToken(accessToken)
                .build();

        try {
            Preference preference = client.create(preferenceRequest, requestOptions);
            return preference;
        } catch (MPApiException e) {
            // Agregar detalles de logging
            System.err.println("MPApiException error: " + e.getApiResponse().getContent());
            e.printStackTrace();
            throw new MPException("Error creating preference: " + e.getMessage(), e);
        }
    }
}
