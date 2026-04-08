package by.itbatia.psp.personservice.repository;

import java.util.UUID;

import by.itbatia.psp.personservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Batsian_SV
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
