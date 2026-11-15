package com.example.likelion14th_springboot.controller;

import com.example.likelion14th_springboot.dto.response.ProductResponseDto;
import com.example.likelion14th_springboot.jwt.JwtTokenProvider;
import com.example.likelion14th_springboot.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) // 인증이 아닌 Controller 응답 검증이 목적이므로 Security 필터 제외
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider; // JwtAuthenticationFilter 빈 생성에 필요한 의존성

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext; // @EnableJpaAuditing 사용 시 필요

    @Test
    @DisplayName("전체 상품 목록을 조회하면 200 OK와 상품 리스트를 JSON 배열로 반환한다.")
    void getAllProducts_Success() throws Exception {
        // given
        ProductResponseDto product1 = new ProductResponseDto(1L, "멋사 티셔츠", 15000, 50, "14기 굿즈");
        ProductResponseDto product2 = new ProductResponseDto(2L, "멋사 키링", 5000, 100, "이쁨");

        given(productService.getAllProducts()).willReturn(List.of(product1, product2));

        // when & then
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("멋사 티셔츠"))
                .andExpect(jsonPath("$[0].price").value(15000))
                .andExpect(jsonPath("$[1].name").value("멋사 키링"))
                .andExpect(jsonPath("$[1].stock").value(100));
    }
}
