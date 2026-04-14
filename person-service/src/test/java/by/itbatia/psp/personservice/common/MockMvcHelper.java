package by.itbatia.psp.personservice.common;

import static by.itbatia.psp.personservice.util.ConstantUtil.X_REQUEST_INITIATOR;

import java.nio.charset.StandardCharsets;

import by.itbatia.psp.common.dto.IndividualCreateRequest;
import by.itbatia.psp.common.dto.IndividualUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

/**
 * @author Batsian_SV
 */
@Component
public class MockMvcHelper {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //-//-//-//-// -----------------------------------------------   CREATE   ----------------------------------------------- //-//-//-//-//

    public ResultActions performCreating(IndividualCreateRequest request) throws Exception {
        return performCreating(request, "fake");
    }

    public ResultActions performCreating(IndividualCreateRequest request, String header) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/individuals")
                .header(X_REQUEST_INITIATOR, "fake")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))
        );
    }

    //-//-//-//-// ------------------------------------------------   READ   ------------------------------------------------ //-//-//-//-//

    public ResultActions performGetById(String id) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/individuals/{id}", id)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
        );
    }

    public ResultActions performGetByEmail(String email) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/individuals/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
        );
    }

    //-//-//-//-// -----------------------------------------------   UPDATE   ----------------------------------------------- //-//-//-//-//

    public ResultActions performUpdating(IndividualUpdateRequest request) throws Exception {
        return performUpdating(request, "fake");
    }

    public ResultActions performUpdating(IndividualUpdateRequest request, String header) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .put("/api/v1/individuals")
                .header(X_REQUEST_INITIATOR, header)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))
        );
    }

    public ResultActions performUpdatingWithoutHeader(IndividualUpdateRequest request) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .put("/api/v1/individuals")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request))
        );
    }

    //-//-//-//-// -----------------------------------------------   DELETE   ----------------------------------------------- //-//-//-//-//

    public ResultActions performDeleting(String id) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/api/v1/individuals/{id}", id)
                .header(X_REQUEST_INITIATOR, "fake")
        );
    }
}
