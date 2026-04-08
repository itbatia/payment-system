package by.itbatia.psp.personservice.repository;

import java.util.Optional;
import java.util.UUID;

import by.itbatia.psp.personservice.entity.IndividualEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Batsian_SV
 */
public interface IndividualRepository extends JpaRepository<IndividualEntity, UUID> {

    @Query("""
        SELECT i FROM IndividualEntity i
        LEFT JOIN FETCH i.user u
        LEFT JOIN FETCH u.address a
        LEFT JOIN FETCH a.country c
        WHERE i.id = :id
        """)
    @NonNull
    Optional<IndividualEntity> findById(@NonNull @Param("id") UUID id);

    @Query("""
        SELECT i FROM IndividualEntity i
        LEFT JOIN FETCH i.user u
        LEFT JOIN FETCH u.address a
        LEFT JOIN FETCH a.country c
        WHERE u.email = :email
        """)
    Optional<IndividualEntity> findByUserEmail(@Param("email") String email);
}
