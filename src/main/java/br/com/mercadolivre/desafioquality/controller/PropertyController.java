package br.com.mercadolivre.desafioquality.controller;

import br.com.mercadolivre.desafioquality.dto.mapper.PropertyMapper;
import br.com.mercadolivre.desafioquality.dto.request.PropertyDTO;
import br.com.mercadolivre.desafioquality.dto.response.PropertyCreatedDTO;
import br.com.mercadolivre.desafioquality.dto.response.PropertyResponseDTO;
import br.com.mercadolivre.desafioquality.dto.response.PropertyValueDTO;
import br.com.mercadolivre.desafioquality.exceptions.*;
import br.com.mercadolivre.desafioquality.models.Property;
import br.com.mercadolivre.desafioquality.services.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/property")
@AllArgsConstructor
public class PropertyController {

    final private PropertyService propertyService;

    @GetMapping("/")
    public ResponseEntity<List<PropertyResponseDTO>> requestPropertyList() throws DatabaseReadException, DatabaseManagementException {
        List<Property> properties = propertyService.ListProperties();

        List<PropertyResponseDTO> response = PropertyMapper.toPropertyResponse(properties);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/property-value/{propertyId}")
    public ResponseEntity<PropertyValueDTO> requestPropertyValue(@PathVariable UUID propertyId) throws DatabaseReadException, DatabaseManagementException {

        Property property = propertyService.calcPropertyPrice(propertyId);

        PropertyValueDTO propertyResponse = PropertyMapper.toPropertyValueResponse(property);

        return ResponseEntity.status(HttpStatus.OK).body(propertyResponse);
    }

  
    @PostMapping
    public ResponseEntity<PropertyCreatedDTO> createProperty(
            @RequestBody @Valid PropertyDTO propertyToAddDTO,
            UriComponentsBuilder uriBuilder
    ) throws DatabaseManagementException, DatabaseWriteException, DbEntryAlreadyExists, DatabaseReadException, NeighborhoodNotFoundException {

        Property propertyToAdd = propertyToAddDTO.toModel();

        Property addedProperty = propertyService.addProperty(propertyToAdd);

        URI uri = uriBuilder
                .path("/api/v1/property/{id}")
                .buildAndExpand(addedProperty.getId())
                .toUri();

        return ResponseEntity.created(uri).body(PropertyCreatedDTO.fromModel(addedProperty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> findPropertyByID(@PathVariable UUID id) throws DatabaseReadException, DatabaseManagementException {
        Property foundedProperty = propertyService.find(id);

        PropertyDTO foundedPropertyDTO = PropertyDTO
                .builder()
                .id(foundedProperty.getId())
                .propName(foundedProperty.getPropName())
                .propDistrict(foundedProperty.getPropDistrict())
                .propRooms(foundedProperty.getPropRooms())
                .build();

        return ResponseEntity.ok(foundedPropertyDTO);
    }

    @GetMapping("/property-area/{propertyId}")
    public ResponseEntity<PropertyValueDTO> requestPropertyArea(@PathVariable UUID propertyId) throws DatabaseReadException, DatabaseManagementException {

        Property property = propertyService.findProperty(propertyId);

        Double totalArea = propertyService.calcPropertyArea(property);

        PropertyValueDTO propertyResponse = PropertyMapper.toPropertyResponseArea(property, totalArea);

        return ResponseEntity.status(HttpStatus.OK).body(propertyResponse);
    }

}
