package by.itbatia.psp.personservice.entity;

import by.itbatia.psp.personservice.listener.CustomRevisionListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * @author Batsian_SV
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "person", name = "revinfo")
@RevisionEntity(CustomRevisionListener.class)
public class CustomRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "rev")
    private long id;

    @RevisionTimestamp
    @Column(name = "revtstmp")
    private long timestamp;

    @Column(name = "modified_by")
    private String modifiedBy;
}
