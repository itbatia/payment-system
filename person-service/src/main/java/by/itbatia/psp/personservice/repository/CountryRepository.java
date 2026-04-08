package by.itbatia.psp.personservice.repository;

import by.itbatia.psp.personservice.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Batsian_SV
 */
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}
