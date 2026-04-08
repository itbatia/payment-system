package by.itbatia.psp.personservice.service.impl;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import by.itbatia.psp.common.dto.internal.AddressUpdateRequest;
import by.itbatia.psp.common.dto.internal.IndividualCreateRequest;
import by.itbatia.psp.common.dto.internal.IndividualUpdateRequest;
import by.itbatia.psp.common.dto.internal.IndividualResponse;
import by.itbatia.psp.personservice.entity.AddressEntity;
import by.itbatia.psp.personservice.entity.CountryEntity;
import by.itbatia.psp.personservice.entity.IndividualEntity;
import by.itbatia.psp.personservice.entity.UserEntity;
import by.itbatia.psp.personservice.enums.Status;
import by.itbatia.psp.personservice.exception.api.BadRequestApiException;
import by.itbatia.psp.personservice.exception.api.NotFoundApiException;
import by.itbatia.psp.personservice.exception.api.UniqueConstraintViolationApiException;
import by.itbatia.psp.personservice.mapper.AddressMapper;
import by.itbatia.psp.personservice.mapper.IndividualMapper;
import by.itbatia.psp.personservice.repository.IndividualRepository;
import by.itbatia.psp.personservice.service.CountryService;
import by.itbatia.psp.personservice.service.IndividualService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Batsian_SV
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndividualServiceImpl implements IndividualService {

    private final EntityManager entityManager;
    private final AddressMapper addressMapper;
    private final CountryService countryService;
    private final IndividualMapper individualMapper;
    private final IndividualRepository individualRepository;

    //-//-//-//-// -----------------------------------------------   create   ----------------------------------------------- //-//-//-//-//

    @Override
    @Transactional
    public IndividualResponse create(IndividualCreateRequest request) throws NotFoundApiException, UniqueConstraintViolationApiException {
        try {
            IndividualEntity individual = individualMapper.toIndividualEntity(request);

            linkCountryAndAddress(individual);
            individual.getUser().enrichFilled();

            IndividualEntity savedIndividual = individualRepository.save(individual);
            log.info("Individual saved with [id={}]", savedIndividual.getId());

            return individualMapper.toIndividualResponse(savedIndividual);

        } catch (DataIntegrityViolationException exception) {
            throw new UniqueConstraintViolationApiException("User with this email already exists", exception);
        }
    }

    /**
     * @param individual Transient entity
     */
    private void linkCountryAndAddress(IndividualEntity individual) {
        AddressEntity address = individual.getUser().getAddress();

        if (address == null) {
            return;
        }
        Long countryId = address.getCountry().getId();
        CountryEntity country = countryService.getById(countryId);
        address.setCountry(country);
    }

    //-//-//-//-// ----------------------------------------------   getById   ----------------------------------------------- //-//-//-//-//

    @Override
    public IndividualResponse getById(UUID id) throws NotFoundApiException {
        IndividualEntity individual = findById(id);
        return individualMapper.toIndividualResponse(individual);
    }

    private IndividualEntity findById(UUID id) throws NotFoundApiException {
        IndividualEntity individual = individualRepository.findById(id).orElseThrow(() ->
            new NotFoundApiException(String.format("Individual by [id=%s] not found", id))
        );

        log.info("Individual found by [id={}]", id);
        return individual;
    }

    //-//-//-//-// ---------------------------------------------   getByEmail   --------------------------------------------- //-//-//-//-//

    @Override
    public IndividualResponse getByEmail(String email) throws NotFoundApiException {
        IndividualEntity individual = individualRepository.findByUserEmail(email).orElseThrow(() ->
            new NotFoundApiException(String.format("Individual by user [email=%s] not found", email))
        );

        log.info("Individual found by user [email={}]", email);
        return individualMapper.toIndividualResponse(individual);
    }

    //-//-//-//-// -----------------------------------------------   update   ----------------------------------------------- //-//-//-//-//

    @Override
    @Transactional
    public IndividualResponse update(IndividualUpdateRequest request) throws NotFoundApiException, UniqueConstraintViolationApiException {
        try {
            IndividualEntity existingIndividual = findById(request.getId());
            updateExistingIndividual(existingIndividual, request);

            existingIndividual.getUser().enrichFilled();
            IndividualEntity updatedIndividual = individualRepository.save(existingIndividual);
            entityManager.flush();

            log.info("Individual [id={}] updated", updatedIndividual.getId());
            return individualMapper.toIndividualResponse(updatedIndividual);

        } catch (ConstraintViolationException exception) {
            throw new UniqueConstraintViolationApiException("User with this email already exists", exception);
        }
    }

    private void updateExistingIndividual(IndividualEntity existingIndividual, IndividualUpdateRequest request) throws NotFoundApiException {
        individualMapper.updateIndividualFromRequest(request, existingIndividual);
        updateExistingUserWithAddress(existingIndividual, request.getUser().getAddress());
    }

    private void updateExistingUserWithAddress(IndividualEntity existingIndividual, AddressUpdateRequest requestAddress) throws NotFoundApiException {
        UserEntity existingUser = existingIndividual.getUser();
        AddressEntity existingAddress = existingUser.getAddress();

        boolean isNullExistingAddress = Objects.isNull(existingAddress);
        boolean isNullRequestAddress = Objects.isNull(requestAddress);

        if (isNullExistingAddress && isNullRequestAddress) {
            return;
        }
        if (isNullExistingAddress) {
            AddressEntity addressEntity = addressMapper.toAddressEntity(requestAddress);
            addressEntity.setUser(existingUser);
            existingUser.setAddress(addressEntity);
            return;
        }
        if (isNullRequestAddress) {
            existingUser.setAddress(null);
            existingAddress.setUser(null);
            existingAddress.setArchivedAt(OffsetDateTime.now());
            return;
        }
        if (!Objects.equals(existingAddress.getCountry().getId(), requestAddress.getCountryId())) {
            CountryEntity countryEntity = countryService.getById(requestAddress.getCountryId());
            existingAddress.setCountry(countryEntity);
        }
        addressMapper.updateAddressFromRequest(requestAddress, existingAddress);
    }

    //-//-//-//-// -----------------------------------------------   delete   ----------------------------------------------- //-//-//-//-//

    @Override
    @Transactional
    public void delete(UUID id) throws NotFoundApiException, BadRequestApiException {
        IndividualEntity individual = findById(id);

        if (individual.isDeleted()) {
            throw new BadRequestApiException(String.format("Individual [id=%s] has already been deleted", id));
        }

        AddressEntity address = individual.getUser().getAddress();

        if (address != null) {
            address.setArchivedAt(OffsetDateTime.now());
        }

        individual.setStatus(Status.DELETED);
        individualRepository.save(individual);

        log.info("Individual [id={}] deleted", id);
    }
}
