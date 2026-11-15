package com.example.likelion14th_springboot.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("계산기(Calculator) 단위 테스트")
class CalculatorTest {

    private Calculator calculator;

    // 1. 생명주기 어노테이션: 각 테스트가 실행되기 직전에 매번 새 계산기를 만든다.
    // (테스트 간의 격리와 독립성을 보장하기 위함!)
    @BeforeEach
    void setUp() {
        System.out.println("--- 테스트 시작 전: 새로운 계산기 객체 초기화 완료 ---");
        calculator = new Calculator();
    }

    // 2. @Nested: 비슷한 성격의 테스트들을 그룹화하여 계층적으로 보여줌
    @Nested
    @DisplayName("덧셈 연산 테스트 그룹")
    class AddTest {

        @Test
        @DisplayName("성공: 두 숫자를 더하면 올바른 합이 반환된다.")
        void add_Success() {
            // Given (준비)
            int num1 = 5;
            int num2 = 3;

            // When (실행)
            int result = calculator.add(num1, num2);

            // Then (검증)
            assertThat(result).isEqualTo(8);
        }

        // 3. @ParameterizedTest: 여러 개의 값으로 동일한 로직을 반복 테스트할 때 좋음
        // @CsvSource에 적힌 "입력값1, 입력값2, 기대결과"가 파라미터로 들어감
        @DisplayName("성공: 다양한 숫자들의 덧셈 결과를 한 번에 다 확인한다.")
        @ParameterizedTest
        @CsvSource({
                "1, 2, 3",
                "10, 20, 30",
                "0, 0, 0",
                "-5, 5, 0"
        })
        void add_Parameterized_Success(int num1, int num2, int expected) {
            // ParameterizedTest를 사용하면 파라미터가 들어오므로 Given 과정이 생략됨

            // When
            int result = calculator.add(num1, num2);

            // Then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("나눗셈 연산 테스트 그룹")
    class DivideTest {

        @Test
        @DisplayName("성공: 정상적인 두 숫자를 나누면 올바른 몫이 반환된다.")
        void divide_Success() {
            // Given
            int num1 = 10;
            int num2 = 2;

            // When
            double result = calculator.divide(num1, num2);

            // Then
            assertThat(result).isEqualTo(5.0);
        }

        // 4. 예외 방어 로직 검증
        @Test
        @DisplayName("실패: 분모가 0이면 IllegalArgumentException 예외가 터져야 한다.")
        void divide_Fail_ByZero() {
            // Given
            int num1 = 10;
            int num2 = 0; // 0으로 나누기 시도

            // When & Then
            // assertThatThrownBy 내부에 에러가 터질 로직을 넣고 체이닝으로 검증
            assertThatThrownBy(() -> calculator.divide(num1, num2))
                    .isInstanceOf(IllegalArgumentException.class) // 기대하는 예외 타입 확인
                    .hasMessage("0으로 나눌 수 없습니다.");      // 튀어나온 에러 메시지 텍스트까지 완벽히 일치하는지 확인!
        }
    }
}