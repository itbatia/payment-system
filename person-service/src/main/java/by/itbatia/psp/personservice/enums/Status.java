package by.itbatia.psp.personservice.enums;

/**
 * @author Batsian_SV
 */
public enum Status {

    ACTIVE, DELETED;

    public boolean isDeleted() {
        return this == DELETED;
    }
}
