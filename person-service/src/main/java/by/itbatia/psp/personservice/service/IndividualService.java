package by.itbatia.psp.personservice.service;

import java.util.UUID;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.common.dto.IndividualResponse;
import by.itbatia.psp.personservice.exception.api.BadRequestApiException;
import by.itbatia.psp.personservice.exception.api.NotFoundApiException;
import by.itbatia.psp.personservice.exception.api.UniqueConstraintViolationApiException;

/**
 * @author Batsian_SV
 */
public interface IndividualService {

    IndividualResponse create(IndividualCreateRequest request) throws NotFoundApiException, UniqueConstraintViolationApiException;

    IndividualResponse getById(UUID id) throws NotFoundApiException;

    IndividualResponse getByEmail(String email) throws NotFoundApiException;

    IndividualResponse update(IndividualUpdateRequest request) throws NotFoundApiException, UniqueConstraintViolationApiException;

    void delete(UUID id) throws NotFoundApiException, BadRequestApiException;
}
