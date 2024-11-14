import com.github.javafaker.Faker;
import models.RequestCreateUserRootModel;
import models.ResponseCreateUserRootModel;
import models.SingleUserRootModel;
import models.UserListRootModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.ReqresSpecs.*;

public class ReqresApiTests extends TestBase {
    private final Faker faker = new Faker();

    @DisplayName("Проверка количества пользователей на одной странице")
    @Test
    public void totalUsersOnPageTest() {
        int page = faker.number().numberBetween(1, 2);
        int expectedDataSize = 6;
        UserListRootModel userListRootModel = step("Отправить GET запрос с существующей страницей", () ->
                given(requestWithoutBodySpecification)
                        .queryParam("page", page)
                        .get("/users")
                        .then()
                        .spec(responseSpecificationWithStatus200)
                        .extract().as(UserListRootModel.class));
        step("Проверить, что в массиве data ожидаемое количество пользователей", () ->
                assertThat(userListRootModel.getData().size()).isEqualTo(expectedDataSize));
    }

    @DisplayName("Проверка, что список пользователей пустой на не существующей странице")
    @Test
    public void emptyListUsersPageTest() {
        int page = faker.number().numberBetween(4, 50);
        int expectedDataSize = 0;
        UserListRootModel userListRootModel = step("Отправить GET запрос с не существующей страницей", () ->
                given(requestWithoutBodySpecification)
                        .queryParam("page", page)
                        .get("/users")
                        .then()
                        .spec(responseSpecificationWithStatus200)
                        .extract().as(UserListRootModel.class));
        step("Проверить, что в массиве data 0 пользователей", () ->
                assertThat(userListRootModel.getData().size()).isEqualTo(expectedDataSize));
    }

    static Stream<Arguments> userNamesAndIdShouldBeEqualsTest() {
        return Stream.of(
                Arguments.of(4, "Eve", "Holt"),
                Arguments.of(9, "Tobias", "Funke")
        );
    }

    @MethodSource()
    @DisplayName("Проверка имени и фамилии пользователя по id: ")
    @ParameterizedTest(name = "У пользователя с id = {0}, имя - {1}, фамилия {2}")
    public void userNamesAndIdShouldBeEqualsTest(int id, String firstName, String lastName) {
        SingleUserRootModel singleUserRootModel = step("Отправить GET запрос с id существующего пользователя", () ->
                given(requestWithoutBodySpecification)
                        .pathParam("id", id)
                        .get("/users/{id}")
                        .then()
                        .spec(responseSpecificationWithStatus200)
                        .extract().as(SingleUserRootModel.class));
        step("Проверить, что id совпадают", () ->
                assertThat(singleUserRootModel.getData().getId()).isEqualTo(id));
        step("Проверить, что firstName совпадают", () ->
                assertThat(singleUserRootModel.getData().getFirstName()).isEqualTo(firstName));
        step("Проверить, что lastName совпадают", () ->
                assertThat(singleUserRootModel.getData().getLastName()).isEqualTo(lastName));
    }

    @DisplayName("Проверка создания пользователя с именем и работой")
    @Test
    public void createUserWithNameAndJobTest() {
        String name = faker.name().firstName();
        String jobName = "QA";
        RequestCreateUserRootModel userBody = new RequestCreateUserRootModel(name, jobName);
        ResponseCreateUserRootModel responseCreateUserRootModel = step("Отправить POST запрос на создание пользователя с именем и работой в теле", () ->
                given(requestWithBodySpecification)
                        .body(userBody)
                        .post("/users")
                        .then()
                        .spec(responseSpecificationWithStatus201)
                        .extract().as(ResponseCreateUserRootModel.class));
        step("Проверить, что в теле ответа такое же имя, что и при отправке запроса", () ->
                assertThat(responseCreateUserRootModel.getName()).isEqualTo(name));
        step("Проверить, что в теле ответа такая же работа, что и при отправке запроса", () ->
                assertThat(responseCreateUserRootModel.getJob()).isEqualTo(jobName));
    }

    @DisplayName("Проверка создания пользователя без указания работы")
    @Test
    public void createUserWithoutJobTest() {
        String name = faker.name().firstName();
        RequestCreateUserRootModel userBody = new RequestCreateUserRootModel();
        userBody.setName(name);
        ResponseCreateUserRootModel responseCreateUserRootModel = step("Отправить POST запрос на создание пользователя с именем, но без работы в теле", () ->
                given(requestWithBodySpecification)
                        .body(userBody)
                        .post("/users")
                        .then()
                        .spec(responseSpecificationWithStatus201)
                        .extract().as(ResponseCreateUserRootModel.class));
        step("Проверить, что в теле ответа такое же имя, что и при отправке запроса", () ->
                assertThat(responseCreateUserRootModel.getName()).isEqualTo(name));
        step("Проверить, что в теле ответа работа равна null", () ->
                assertThat(responseCreateUserRootModel.getJob()).isNull());
    }

    @DisplayName("Проверка создания пользователя без указания имени")
    @Test
    public void createUserWithoutNameTest() {
        String jobName = "QA";
        RequestCreateUserRootModel userBody = new RequestCreateUserRootModel();
        userBody.setJob(jobName);
        ResponseCreateUserRootModel responseCreateUserRootModel = step("Отправить POST запрос на создание пользователя с работой, но без имени в теле", () ->
                given(requestWithBodySpecification)
                        .body(userBody)
                        .post("/users")
                        .then()
                        .spec(responseSpecificationWithStatus201)
                        .extract().as(ResponseCreateUserRootModel.class));
        step("Проверить, что в теле ответа такая же работа, что и при отправке запроса", () ->
                assertThat(responseCreateUserRootModel.getJob()).isEqualTo(jobName));
        step("Проверить, что в теле ответа имя равно null", () ->
                assertThat(responseCreateUserRootModel.getName()).isNull());
    }
}
