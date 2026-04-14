package by.itbatia.psp.personservice.listener;

import by.itbatia.psp.personservice.entity.CustomRevisionEntity;
import by.itbatia.psp.personservice.util.AuditContextUtil;
import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

/**
 * @author Batsian_SV
 */
@Component
public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {

        CustomRevisionEntity entity = (CustomRevisionEntity) revisionEntity;
        String requestInitiator = AuditContextUtil.get();
        entity.setModifiedBy(requestInitiator != null ? requestInitiator : "unknown");
    }
}
