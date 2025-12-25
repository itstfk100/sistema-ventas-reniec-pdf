package org.cibertec.proyectof.services;

import org.cibertec.proyectof.dtos.ReniecResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReniecService {

    @Value("${decolecta.reniec.url}")
    private String apiUrl;

    @Value("${decolecta.reniec.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public ReniecResponse consultarDni(String dni) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = apiUrl + "?numero=" + dni;

        ResponseEntity<ReniecResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, ReniecResponse.class);

        return response.getBody();
    }
}
