package com.example.likelion14th_springboot.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("비밀번호 검증기 단위 테스트")
class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Nested
    @DisplayName("비밀번호 검증 성공 테스트 그룹")
    class SuccessTest {

        @Test
        @DisplayName("성공: 8자 이상의 정상적인 비밀번호가 들어오면 예외가 발생하지 않는다.")
        void validate_Success() {
            // Given
            String validPassword = "serverwizard"; // 12글자의 정상 비밀번호

            // When & Then
            // 리턴값이 없는 void 메서드가 '아무런 에러 없이 무사히 통과함'을 검증할 때 사용함
            assertThatCode(() -> passwordValidator.validate(validPassword))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("비밀번호 검증 실패 테스트 그룹")
    class FailTest {

        // @ValueSource: 여러 개의 잘못된 문자열들을 한 번에 주입하여 테스트
        @DisplayName("실패: 비밀번호가 8자 미만이면 예외가 터져야 한다.")
        @ParameterizedTest(name = "테스트 값: {0}") // {0}에는 아래 배열의 문자열이 순서대로 들어감.
        @ValueSource(strings = {"1234567", "abcdefg", "pwd", "1"})
        void validate_Fail_Length(String invalidPassword) {

            // When & Then
            assertThatThrownBy(() -> passwordValidator.validate(invalidPassword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(PasswordValidator.WRONG_PASSWORD_LENGTH_EXCEPTION_MESSAGE);
        }

        // @NullAndEmptySource: 'null'과 '빈 문자열("")'을 한 번에 파라미터로 주입해 주는 어노테이션
        @DisplayName("실패: 비밀번호가 null이거나 비어있으면 예외가 터져야 한다.")
        @ParameterizedTest
        @NullAndEmptySource
        void validate_Fail_NullOrEmpty(String invalidPassword) {

            // When & Then
            assertThatThrownBy(() -> passwordValidator.validate(invalidPassword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(PasswordValidator.WRONG_PASSWORD_EMPTY_EXCEPTION_MESSAGE);
        }
    }
}