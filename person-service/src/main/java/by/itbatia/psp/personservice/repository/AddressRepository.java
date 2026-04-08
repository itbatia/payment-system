package by.itbatia.psp.personservice.repository;

import java.util.UUID;

import by.itbatia.psp.personservice.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Batsian_SV
 */
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
}
