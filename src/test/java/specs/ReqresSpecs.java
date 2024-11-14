package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;

public class ReqresSpecs {
    public static RequestSpecification requestWithoutBodySpecification = with()
            .filter(withCustomTemplates())
            .log().all();
    public static RequestSpecification requestWithBodySpecification = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType(ContentType.JSON);
    public static ResponseSpecification responseSpecificationWithStatus200 =
            new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .log(LogDetail.ALL)
                    .build();
    public static ResponseSpecification responseSpecificationWithStatus201 =
            new ResponseSpecBuilder()
                    .expectStatusCode(201)
                    .log(LogDetail.ALL)
                    .build();
}
