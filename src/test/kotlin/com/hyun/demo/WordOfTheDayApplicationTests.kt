package com.hyun.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles // 이 import가 필요합니다.

@SpringBootTest
@ActiveProfiles("dev") // 이 부분을 추가/확인하세요
class WordOfTheDayApplicationTests {

	@Test
	fun contextLoads() {
		// 컨텍스트 로딩 테스트
	}
}