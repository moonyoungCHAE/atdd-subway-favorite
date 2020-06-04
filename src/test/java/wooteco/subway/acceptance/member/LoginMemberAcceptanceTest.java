package wooteco.subway.acceptance.member;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.AcceptanceTest;
import wooteco.subway.service.member.dto.MemberResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginMemberAcceptanceTest extends AcceptanceTest {

    /**
     * Scenario: 각 회원이 자신의 정보를 관리를 한다
     * When: 회원 가입 요청을 한다.
     * Then: 회원 가입이 완료되었다.
     * <p>
     * When: 로그인 요청을 한다.
     * Then: 로그인이 완료되었다.
     * And: 토큰이 발급된다.
     * <p>
     * When: 나의 정보를 요청한다.
     * Then: 나의 정보를 받는다.
     *
     * <p>
     * When: 회원 정보 수정 요청을 한다.
     * Then: 회원 정보가 수정되었다.
     * <p>
     * When: 회원 탈퇴 요청을 한다.
     * Then: 회원 탈퇴가 완료되었다.
     */

    @Test
    void manageLoginMember() {
        join(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        Response response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.cookie("Bearer")).isNotEmpty();

        String token = response.cookie("Bearer");
        MemberResponse member = getMemberByToken(token);
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(member.getName()).isEqualTo(TEST_USER_NAME);

        updateMember("NEW_" + TEST_USER_NAME, "NEW_" + TEST_USER_PASSWORD, token);

        MemberResponse updatedMember = getMemberByToken(token);
        assertThat(updatedMember.getId()).isNotNull();
        assertThat(updatedMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);

        deleteMember(token);
    }

    private MemberResponse getMemberByToken(String token) {
        return given()
                .cookie("Bearer", token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberResponse.class);
    }

    private void updateMember(String name, String password, String token) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);

        given().body(params)
                .cookie("Bearer", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
