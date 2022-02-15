package nktori.dummybank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import nktori.dummybank.IntegrationTest;
import nktori.dummybank.builders.TransactionRequestBuilder;
import nktori.dummybank.dto.TransactionRequest;
import nktori.dummybank.service.AccountApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionControllerIT extends IntegrationTest {

    @Autowired
    private AccountApplicationService accountService;

    @Test
    public void transactionShouldSuccessfullyBeMade() throws JsonProcessingException {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909)
                .getSchema(TransactionControllerIT.class.getResourceAsStream("/transactionSchema.json"));

        Integer account1Balance = accountService.getAccountById(1).getBalance();
        Integer account2Balance = accountService.getAccountById(2).getBalance();

        TransactionRequest request = TransactionRequestBuilder.builder()
                .withTo(1)
                .withFrom(2)
                .withAmount(1)
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/transaction", request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(Objects.requireNonNull(response.getBody()));

        assertThat(schema.validate((jsonNode))).isEmpty();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(accountService.getAccountById(1).getBalance()).isEqualTo(account1Balance + 1);
        assertThat(accountService.getAccountById(2).getBalance()).isEqualTo(account2Balance - 1);
    }

    @Test
    public void transactionShouldFailIfAmountIsInvalid() {
        TransactionRequest request = TransactionRequestBuilder.builder()
                .withTo(1)
                .withFrom(2)
                .withAmount(0)
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/transaction", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid transaction amount. Cannot be less than 1");
    }

    @Test
    public void transactionShouldFailIfPayeeAndPayerTheSame() {
        TransactionRequest request = TransactionRequestBuilder.builder()
                .withTo(1)
                .withFrom(1)
                .withAmount(1)
                .build();


        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/transaction", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Payer and Payee cannot have the same id");
    }

    @Test
    public void transactionShouldFailIfInsufficientFunds() {
        TransactionRequest request = TransactionRequestBuilder.builder()
                .withTo(1)
                .withFrom(2)
                .withAmount(Integer.MAX_VALUE)
                .build();


        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/transaction", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Account Id#2 has insufficient funds for this transaction");
    }
}
