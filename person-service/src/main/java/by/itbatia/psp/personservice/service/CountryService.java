package by.itbatia.psp.personservice.service;

import by.itbatia.psp.personservice.entity.CountryEntity;
import by.itbatia.psp.personservice.exception.api.NotFoundApiException;

/**
 * @author Batsian_SV
 */
public interface CountryService {

    CountryEntity getById(Long id) throws NotFoundApiException;
}
