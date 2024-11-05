package com.lunionlab.turbo_restaurant.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunionlab.turbo_restaurant.Enums.DeletionEnum;
import com.lunionlab.turbo_restaurant.model.CollectionModel;
import com.lunionlab.turbo_restaurant.repository.CollectionRepository;
import com.lunionlab.turbo_restaurant.response.CollectionResponse;
import com.lunionlab.turbo_restaurant.utilities.Report;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CollectionService {
    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    GenericService genericService;

    @Value("${erp.server.address}")
    private String ERP_ADDRESS;

    public Object getCollections() {
        String endpoint = ERP_ADDRESS + "/turbo/erp/collection/resto/get";
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseEntity<String> response = genericService.httpGet(endpoint);
            if (response.getStatusCode().is2xxSuccessful()) {
                List<CollectionResponse> collectionResponses = mapper.readValue(response.getBody(),
                        new TypeReference<List<CollectionResponse>>() {
                        });
                for (CollectionResponse collectionResponse : collectionResponses) {
                    Boolean isExist = collectionRepository.existsByLibelleAndDeleted(collectionResponse.getLibelle(),
                            DeletionEnum.NO);
                    if (isExist) {
                        CollectionModel collection = collectionRepository
                                .findFirstByLibelleAndDeleted(collectionResponse.getLibelle(), DeletionEnum.NO).get();
                        collection.setLibelle(collectionResponse.getLibelle());
                        collection.setDescription(collectionResponse.getDescription());
                        collection.setDeleted(collectionResponse.getDeleted());
                        collection.setPicture(collectionResponse.getPicture());
                        collection.setPictureUrl(collectionResponse.getPictureUrl());
                        collection.setStatus(collectionResponse.getStatus());
                        collection.setDateEdition(new Date());
                        collectionRepository.save(collection);
                    } else {
                        CollectionModel collectionModel = new CollectionModel(collectionResponse.getLibelle(),
                                collectionResponse.getDescription(), collectionResponse.getPicture(),
                                collectionResponse.getPictureUrl());
                        collectionRepository.save(collectionModel);
                    }
                }
                return ResponseEntity.ok(collectionResponses);
            }
        } catch (HttpClientErrorException httpex) {
            log.error(httpex.getMessage());
            return new ResponseEntity<String>(httpex.getMessage(), httpex.getStatusCode());

        } catch (Exception e) {
            log.error(e.getMessage());
            return Report.internalError(e.getMessage());
        }
        return ResponseEntity.badRequest().body("can't get collection resource");
    }

}
