package com.ohgiraffers.parameterized.section01.params;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.Month;
import java.util.stream.Stream;

public class ParameterizedTests {

    /* 수업목표. junit-jupiter-params 모듈을 이용하여 파라미터를 이용한 테스트를 작성할 수 있다.*/

    /*필기.
     * 테스트 시 여러 값들을 이용하여 검증을 해야 하는 경우 모든 경우의 수 만큼 테스트 메소드를 작성해야 한다.
     * parameterized-test는 @ParameterizedTest 어노테이션을 대체하여 사용하며,
     * 이 경우 테스트 메소드에 매개변수를 선언할 수 있다.(일반적인 테스트에서는 매개변수를 사용 할 수 없다.)
     * 파라미터로 전달할 값의 목록 만큼 반복적으로 테스트 메소드를 실행하며, 반복 실행 시 마다 준비된 값 목록을
     * 매개변수로 전달하여 테스트 코드를 실행하게 된다.
     * given when then 패턴에서 사전에 테스트를 위해 준비하던 given 부분을 대체할 수 있다.
     */

    /* 목차. 1. @ValueSource 이용한 parameter value 목록 지정 */
    /* 필기.
     * @ValueSource를 이용하여 한 개의 파라미터로 전달할 값들의 목록을 지정할 수 있다.
     * 이 때 지원하는 타입은 다음과 같다.
     * short, byte, int, long, float, double, char, java.lang.String, java.lang.Class
     */
    @DisplayName("홀수 짝수 판별 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 3, -1, 15, 123})
    void testIsOdd(int number) {

        //when
        boolean result = NumberValidator.isOdd(number);

        //then
        Assertions.assertTrue(result);
    }

    /*목차. 2. @NullSource와 @EmptySource */
    /*필기.
     *기본 자료형 타입은 null 값을 가질 수 없다.
     * 문자열이나 클래스 타입인 경우 null이나 빈 값을 파라미터로 전달하기 위해 사용되는 어노테이션이다.
     * null값과 빈 값을 모두 전달하기 위해 구성된 @NullAndEmptySource도 이용할 수 있다.
     */
    @DisplayName("null값 테스트")
    @ParameterizedTest
    @NullSource
    void testIsNull(String input) {

        //when
        boolean result = StringValidator.isNull(input);

        //then
        Assertions.assertTrue(result);
    }

    @DisplayName("empyt값 테스트")
    @ParameterizedTest
    @EmptySource
    void testIsEmpty(String input) {

        //when
        boolean result = StringValidator.isEmpty(input);

        //then
        Assertions.assertTrue(result);

    }

    @DisplayName("blank값 테스트")
    @ParameterizedTest
    @NullAndEmptySource
    void testIsBlank(String input) {

        //when
        boolean result = StringValidator.isBlank(input);

        //then
        Assertions.assertTrue(result);
    }

    /* 목차. 3. 열거형을 이요한 @EnumSource 활용하기 */
    /* 필기. 열거형에 작성된 타입들을 파라미터로 전달하여 테스트에 활용할 수 있다 */

    @DisplayName("Month에 정의된 타입들이 1~12월 사이에 범위인지 테스트")
    @ParameterizedTest
    @EnumSource(Month.class)
    void testMonthValueIsCollect(Month month){

        //when
        boolean result = DateValidator.isCollect(month);

        //then
        Assertions.assertTrue(result);
    }
    @DisplayName("2월, 4월, 6월, 9월, 11월을 제외한 달이 31일인지 확인")
    @ParameterizedTest
    @EnumSource(
            value = Month.class,
            names = {"FEBRUARY","APRIL","JUNE","SEPTEMBER","NOVEMBER"},
            mode = EnumSource.Mode.EXCLUDE//exclude로 위에 나온 names들을 뺄수있다.
    )
    void testHasThirstyOneDaysLong(Month month){

        //given
        int verifyValue = 31;

        //when
        int actual = DateValidator.getLastDayOf(month);

        //then
        Assertions.assertEquals(verifyValue, actual);
    }
    /* 목차. 4. @CsvSource를 이용한 CSV 리터럴 */
    /* 필기.
     * 입력값과 예상값을 테스트 메소드에 전달하기 위해 사용할 수 있다.
     * 이 경우 여러 매개변수에 값을 전달할 여러 인자 묶음이 필요하게 된다.
     * 이때, @CsvSource 를 사용할 수 있다.
     */
    @DisplayName("영문자를 대문자로 변경하는지 확인")
    @ParameterizedTest
//    @CsvSource({"test,TEST","tEst,TEST","Javascript, JAVASCRIPT"})
    @CsvSource(value = {"test:TEST","tEsT:TEST", "JavaScript:JAVASCRIPT"},
                delimiter = ':'
    )
    void testToUpperCase(String input, String verifyValue){

        //when
        String actual = input.toUpperCase(); //input 값을 대문자로

        //then
        Assertions.assertEquals(verifyValue, actual); //verifyValue결과값과 actual 입력값이 같은가
    }

    @DisplayName("CSV 파일을 읽은 테스트 데이터를 테스트에 활용하는지 확인")
    @ParameterizedTest
    @CsvFileSource(resources = "/parameter-test-data.csv", numLinesToSkip = 2)
    void testUpperCaseWithCSVFileData(String input, String verifyValue){

        //when
        String actual = input.toUpperCase();

        //then
        Assertions.assertEquals(verifyValue, actual);
    }

    /* 목차. 5. @MethodSource를 활용한 메소드 인수 활용하기 */
    /* 필기. Stream을 반환하는 메소드를 만들어서 이를 테스트에 활용 할 수 있다.*/
    private static Stream<Arguments> providerStringSource(){
        return Stream.of(
                Arguments.of("hello world","HELLO WORLD"),
                Arguments.of("JavaScript","JAVASCRIPT"),
                Arguments.of("tEsT","TEST")
        );

    }
    @DisplayName("메소드 소스를 활용한 대문자 변환 테스트")
    @ParameterizedTest
    @MethodSource("providerStringSource")
    void testToUpperCaseWithMethodSource(String input,String verifyValue){

        //when
        String actual = input.toUpperCase();

        //then
        Assertions.assertEquals(verifyValue, actual);
    }

    /* 목차. 6. 인수 변한기 (암시적 변환과 명시적 변환) */
    /* 필기
     * Junit5는 인수로 지정된 String을 Eunm 변환한다. 이처럼 기본적으로 제공하는 변환을 암시적 변환이라고 한다.
     * UUID, LocalDate, LocalDateTime, Year, Month, 파일 및 경로, URL, 열거형 서브클래스 들을 암시적으로 변환해준다
     */
    @DisplayName("암시적 변환 테스트")
    @ParameterizedTest(name = "[{0}] is 30 days long")
    @CsvSource({"APRIL","JUNE","SEPTEMBER","NOVEMBER"})
    void testAutoConverting(Month month){

        //given
        int verifyValue = 30;
        //when
        int actual = DateValidator.getLastDayOf(month);
        //then
        Assertions.assertEquals(verifyValue, actual);
    }
}
