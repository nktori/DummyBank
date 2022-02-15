package nktori.dummybank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import nktori.dummybank.IntegrationTest;
import nktori.dummybank.builders.TransactionOutputBuilder;
import nktori.dummybank.dto.TransactionOutput;
import nktori.dummybank.service.TransactionApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AccountControllerIT extends IntegrationTest {

    @MockBean
    private TransactionApplicationService transactionService;

    @Test
    public void getAccountShouldReturnCorrectly() throws JsonProcessingException {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                .getSchema(TransactionControllerIT.class.getResourceAsStream("/accountSchema.json"));

        List<TransactionOutput> transactionOutputs = List.of(
                TransactionOutputBuilder.builder()
                        .withPayeeId(1)
                        .withPayerId(2)
                        .withAmount(20)
                        .withTimestamp(LocalDateTime.of(2022, 10, 12, 5, 2, 23, 9))
                        .build()
        );
        when(transactionService.getAllTransactions()).thenReturn(transactionOutputs);
        when(transactionService.getTransactionsByAccountId(eq(1), eq(transactionOutputs))).thenCallRealMethod();

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/account/1", String.class);

        JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(response.getBody()));

        assertThat(schema.validate((jsonNode))).isEmpty();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getAccountShouldThrowWhenNoAccountExists() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/account/0", String.class);

        assertThat(response.getBody()).isEqualTo("Account Id#0 not found");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
