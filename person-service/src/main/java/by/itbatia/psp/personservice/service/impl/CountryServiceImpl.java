package by.itbatia.psp.personservice.service.impl;

import by.itbatia.psp.personservice.entity.CountryEntity;
import by.itbatia.psp.personservice.exception.api.NotFoundApiException;
import by.itbatia.psp.personservice.repository.CountryRepository;
import by.itbatia.psp.personservice.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Batsian_SV
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public CountryEntity getById(Long id) throws NotFoundApiException {
        CountryEntity country = countryRepository.findById(id).orElseThrow(() ->
            new NotFoundApiException(String.format("Country by [id=%d] not found", id))
        );

        log.info("Country found by [id={}]", id);
        return country;
    }
}
