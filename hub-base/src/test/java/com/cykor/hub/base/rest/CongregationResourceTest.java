package com.cykor.hub.base.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cykor.hub.base.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class CongregationResourceTest extends BaseIT {

    @Test
    @Sql("/data/congregationData.sql")
    void getAllCongregations_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, roleAdminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/congregations")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", Matchers.equalTo(2))
                    .body("get(0).id", Matchers.equalTo("a9dd4a99-fba6-375a-9494-772b58f95280"));
    }

    @Test
    @Sql("/data/congregationData.sql")
    void getCongregation_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, roleAdminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/congregations/a9dd4a99-fba6-375a-9494-772b58f95280")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("number", Matchers.equalTo(87));
    }

    @Test
    void getCongregation_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, roleAdminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/congregations/234920ea-2540-3ec7-bbee-9efce43ea25e")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createCongregation_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, roleAdminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/congregationDTORequest.json"))
                .when()
                    .post("/api/congregations")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, congregationRepository.count());
    }

    @Test
    void createCongregation_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, roleAdminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/congregationDTORequest_missingField.json"))
                .when()
                    .post("/api/congregations")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("number"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/congregationData.sql")
    void updateCongregation_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, roleAdminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/congregationDTORequest.json"))
                .when()
                    .put("/api/congregations/a9dd4a99-fba6-375a-9494-772b58f95280")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals(72, congregationRepository.findById(UUID.fromString("a9dd4a99-fba6-375a-9494-772b58f95280")).orElseThrow().getNumber());
        assertEquals(2, congregationRepository.count());
    }

    @Test
    @Sql("/data/congregationData.sql")
    void deleteCongregation_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, roleAdminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/congregations/a9dd4a99-fba6-375a-9494-772b58f95280")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, congregationRepository.count());
    }

}
