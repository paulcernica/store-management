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
    private String created;
    private Integer getCreatedId;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static JsonNode product() {
        return MAPPER.createObjectNode()
                .put("name", "macbook")
                .put("price", 150000)
                .put("description", "a product")
                .put("quantity", 10);
    }

    /**
     * Unit test used to create a product in the database, we pass an admin user with ADMIN role, this way we should
     * get the intended result.
     */
    @Test
    void testCreate() {
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("admin", "admin").postForEntity(
                PRODUCT_PATH,
                product(),
                String.class
        );

        assert response.getStatusCode().is2xxSuccessful();
        assertNotNull(response.getBody());
        created = response.getBody();
        getCreatedId = Integer.parseInt(created.substring(created.indexOf(":") + 1, created.indexOf("\"name\"") - 1));
    }

    /**
     * Unit test that verify a request with no auth.
     */
    @Test
    void testCreateNoAuth() {
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                PRODUCT_PATH,
                product(),
                String.class
        );

        assert response.getStatusCode().is4xxClientError();
    }

    /**
     * Unit test that checks if we get at 4xx response code in case an user with the USER role tries to make a POST
     * request.
     */
    @Test
    void testCreateUser() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("user", "user")
                .postForEntity(
                PRODUCT_PATH,
                product(),
                String.class
        );

        assert response.getStatusCode().is4xxClientError();
    }

    /**
     * We test the request to get the product by id, this can be used by an user with USER role too.
     */
    @Test
    void testGetOneEntryById() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("user", "user")
                .getForEntity(
                PRODUCT_PATH.concat("/" + getCreatedId),
                String.class
        );

        assert response.getStatusCode().is2xxSuccessful();
        assertNotNull(response.getBody());
    }

    /**
     * We test the request to update the details of a specific product identified by id in the database.
     *
     * @throws JsonProcessingException
     */
    @Test
    void testUpdate() throws JsonProcessingException {
        String updated = created.replace("macbook", "lenovo");
        JsonNode jsonNode = MAPPER.readTree(updated);
        testRestTemplate.withBasicAuth("admin", "admin").put(
                PRODUCT_PATH.concat("/").concat(jsonNode.get("id").asText()),
                jsonNode,
                String.class
        );

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "admin")
                .getForEntity(
                PRODUCT_PATH.concat("/" + getCreatedId),
                String.class
        );

        assert response.getStatusCode().is2xxSuccessful();
        assertNotNull(created);
        assertNotNull(response.getBody());
        assert !response.getBody().equals(created);

    }

}
