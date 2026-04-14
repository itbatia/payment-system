package by.itbatia.psp.personservice.rest;

import java.util.UUID;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualResponse;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import by.itbatia.psp.personservice.api.IndividualsApi;
import by.itbatia.psp.personservice.service.IndividualService;
import by.itbatia.psp.personservice.util.AuditContextUtil;
import by.itbatia.psp.personservice.util.ThreadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Batsian_SV
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class IndividualController implements IndividualsApi {

    private final IndividualService individualService;

    @Override
    public ResponseEntity<IndividualResponse> create(String requestInitiator, IndividualCreateRequest request) {
        try {
            ThreadUtil.setThreadName(request.getUser().getEmail());
            AuditContextUtil.set(requestInitiator);

            IndividualResponse response = individualService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } finally {
            AuditContextUtil.clear();
        }
    }

    @Override
    public ResponseEntity<IndividualResponse> getById(UUID id) {
        ThreadUtil.setThreadName(id);
        IndividualResponse response = individualService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<IndividualResponse> getByEmail(String email) {
        ThreadUtil.setThreadName(email);
        IndividualResponse response = individualService.getByEmail(email);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<IndividualResponse> update(String requestInitiator, IndividualUpdateRequest request) {
        try {
            ThreadUtil.setThreadName(request.getId());
            AuditContextUtil.set(requestInitiator);

            IndividualResponse response = individualService.update(request);
            return ResponseEntity.ok(response);

        } finally {
            AuditContextUtil.clear();
        }
    }

    @Override
    public ResponseEntity<Void> delete(UUID id, String requestInitiator) {
        try {
            ThreadUtil.setThreadName(id);
            AuditContextUtil.set(requestInitiator);

            individualService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } finally {
            AuditContextUtil.clear();
        }
    }
}
