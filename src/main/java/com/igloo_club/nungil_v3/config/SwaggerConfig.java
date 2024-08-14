package com.igloo_club.nungil_v3.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;

@Configuration
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "Access-Token", in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(security = { @SecurityRequirement(name = "Access-Token") })
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        // pathsToMatch로 원하는 경로의 api만 나오도록 설정
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/v1/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        String title = "눈길 ver 3 API 문서";
        String description = "눈길 ver 3 API 문서입니다. <br> <a href = \"https://www.notion.so/API-ver-3-741c0a47391a4c2cb630b7c657930a6f?pvs=4\">상세 API 명세</a> ";

        Info info = new Info().title(title).description(description).version("1.0.0");

        return new OpenAPI().info(info);
    }

    public ApiResponse createApiResponse(String message, Content content){
        return new ApiResponse().description(message).content(content);
    }

    @Bean
    public GlobalOpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> {
            // 공통으로 사용되는 response 설정
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                ApiResponses apiResponses = operation.getResponses();
                apiResponses.addApiResponse("200", createApiResponse(apiResponses.get("200").getDescription(), apiResponses.get("200").getContent()));
                apiResponses.addApiResponse("400", createApiResponse("Bad Request", null));
                apiResponses.addApiResponse("401", createApiResponse("Access Token Error", null));
                apiResponses.addApiResponse("500", createApiResponse("Server Error", null));
            }));
        };
    }
}
