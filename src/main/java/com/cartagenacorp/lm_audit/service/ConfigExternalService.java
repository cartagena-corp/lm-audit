package com.cartagenacorp.lm_audit.service;

import com.cartagenacorp.lm_audit.dto.ProjectConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class ConfigExternalService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigExternalService.class);

    @Value("${config.service.url}")
    private String configServiceUrl;

    private final RestTemplate restTemplate;

    public ConfigExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectConfigDTO getProjectConfig(String token, UUID projectId) {
        logger.debug("[ConfigExternalService] [getProjectConfig] Obteniendo configuración del proyecto con ID={}", projectId);
        try {
            String url = configServiceUrl + "/" + projectId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ProjectConfigDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    ProjectConfigDTO.class
            );

            ProjectConfigDTO result = response.getBody();

            if (result == null) {
                logger.warn("[ConfigExternalService] [getProjectConfig] La configuración del proyecto con ID={} es nula", projectId);
                return new ProjectConfigDTO();
            }
            return result;

        } catch (HttpClientErrorException.Unauthorized ex) {
            logger.warn("[ConfigExternalService] [getProjectConfig] Token no autorizado: {}", ex.getMessage());
        } catch (HttpClientErrorException.Forbidden ex) {
            logger.warn("[ConfigExternalService] [getProjectConfig] Permisos insuficientes: {}", ex.getMessage());
        } catch (ResourceAccessException ex) {
            logger.warn("[ConfigExternalService] [getProjectConfig] El servicio externo no esta disponible: {}",ex.getMessage());
        }  catch (Exception ex) {
            logger.error("[ConfigExternalService] [getProjectConfig] Error al obtener la configuración del proyecto: {}", ex.getMessage(), ex);
        }
        return new ProjectConfigDTO();
    }
}