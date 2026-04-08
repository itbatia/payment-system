package by.itbatia.psp.personservice.rest;

import java.util.UUID;

import by.itbatia.psp.common.dto.internal.IndividualCreateRequest;
import by.itbatia.psp.common.dto.internal.IndividualResponse;
import by.itbatia.psp.common.dto.internal.IndividualUpdateRequest;
import by.itbatia.psp.personservice.api.IndividualsApi;
import by.itbatia.psp.personservice.service.IndividualService;
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
    public ResponseEntity<IndividualResponse> create(IndividualCreateRequest request) {
        ThreadUtil.setThreadName(request.getUser().getEmail());
        IndividualResponse response = individualService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    public ResponseEntity<IndividualResponse> update(IndividualUpdateRequest request) {
        ThreadUtil.setThreadName(request.getId());
        IndividualResponse response = individualService.update(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        ThreadUtil.setThreadName(id);
        individualService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
