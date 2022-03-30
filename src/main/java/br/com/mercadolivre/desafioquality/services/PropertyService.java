package br.com.mercadolivre.desafioquality.services;

import br.com.mercadolivre.desafioquality.exceptions.DatabaseManagementException;
import br.com.mercadolivre.desafioquality.exceptions.DatabaseReadException;
import br.com.mercadolivre.desafioquality.exceptions.NullIdException;
import br.com.mercadolivre.desafioquality.models.Neighborhood;
import br.com.mercadolivre.desafioquality.models.Property;
import br.com.mercadolivre.desafioquality.repository.ApplicationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.el.PropertyNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final ApplicationRepository<Property, UUID> propertyRepository;

    // São declarados no contexto de classe, pois assim quem se encarrega de instânciar é o Spring
    private final ApplicationRepository<Neighborhood, UUID> neighborhoodRepository;
    //

    public BigDecimal calcPropertyPrice(UUID propertyId) throws NullIdException, DatabaseReadException, DatabaseManagementException {
        if(propertyId == null) {
            throw new NullIdException("O id é nulo!");
        }

        Optional<Property> response = propertyRepository.find(propertyId);

        if(response.isEmpty()) {
            throw new PropertyNotFoundException("Propriedade não encontrada");
        }

        Property requestedProperty = response.get();

        List<Neighborhood> neighborhoodList = neighborhoodRepository.read();

        // TODO: Verificar com o pessoal de cadastro de propriedade se posso usar o get direto ou devo checar primeiro se achou
        Neighborhood requestedPropertyNeighborhood = neighborhoodList
                .stream()
                .filter(n -> n.getNameDistrict().equals(requestedProperty.getPropDistrict()))
                .findFirst().get();

        // Como o método que calcula a área da propriedade não esta pronto vou
        // colocar um hard input qualquer para a área da propriedade
        Double propertyArea = 32.0;

        return requestedPropertyNeighborhood.getValueDistrictM2().multiply(BigDecimal.valueOf(propertyArea));
    }
}