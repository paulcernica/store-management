package com.interviewing.storemanagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest {

    private static final String PRODUCT_PATH = "/product";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    String created;
    Integer getCreatedId;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static JsonNode product() {
        return MAPPER.createObjectNode()
                .put("name", "macbook")
                .put("price", 150000)
                .put("description", "a product")
                .put("quantity", 10);
    }

    @Test
    void testCreate() {
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                PRODUCT_PATH,
                product(),
                String.class
        );

        assertNotNull(response.getBody());
        created = response.getBody();
        getCreatedId = Integer.parseInt(created.substring(created.indexOf(":") + 1, created.indexOf("\"name\"") - 1));
    }

    @Test
    void testGetOneEntryById() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
                PRODUCT_PATH.concat("/" + getCreatedId.toString()),
                String.class
        );

        assertNotNull(response.getBody());
    }

    @Test
    void testUpdate() throws JsonProcessingException {
        String updated = created.replace("macbook", "lenovo");
        JsonNode jsonNode = MAPPER.readTree(updated);
        testRestTemplate.put(
                PRODUCT_PATH.concat("/").concat(jsonNode.get("id").asText()),
                jsonNode,
                String.class
        );

        ResponseEntity<String> response = testRestTemplate.getForEntity(
                PRODUCT_PATH.concat("/" + getCreatedId.toString()),
                String.class
        );
        assertNotNull(created);
        assertNotNull(response.getBody());
        assert !response.getBody().equals(created);

    }

}
