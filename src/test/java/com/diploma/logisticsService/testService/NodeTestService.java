package com.diploma.logisticsService.testService;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class NodeTestService {
    private final RequestSpecification spec = given()
            .contentType(ContentType.JSON);
    @Test
    Response getNodes() {
        Response resp = spec.expect().when().get("/nodes/all");
        return resp;
    }
}
