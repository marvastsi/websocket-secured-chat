package com.marvastsi.securedwebsocketchat.api.swagger.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.marvastsi.securedwebsocketchat.SecuredWebsocketChatApplication;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Component
@EnableSwagger2
@Profile("!prod")
public class SwaggerWebMvcConfigurer implements WebMvcConfigurer {

	@Bean
	public Docket all() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(SecuredWebsocketChatApplication.class.getPackage().getName()))
				.paths(PathSelectors.any()).build()
				.apiInfo(new ApiInfoBuilder().title("secured-websocket-chat API")
						.description("It contains all needed information to interact with this API.")
						.version("v1.0 - ALL").build())
				.enable(true).ignoredParameterTypes(AuthenticationPrincipal.class)
				.globalOperationParameters(globalOperationParameters());
	}

	@Bean
	public Docket platformApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("platform")
				.apiInfo(new ApiInfoBuilder().title("Spring Security API")
						.description("It contains all needed information to interact with this API.")
						.version("v1.0 - PLATFORM").build())
				.select().paths(PathSelectors.ant("/platform/**")).build()
				.ignoredParameterTypes(AuthenticationPrincipal.class)
				.globalOperationParameters(globalOperationParameters());
	}
	
	private List<Parameter> globalOperationParameters() {
		ParameterBuilder parameterToken = new ParameterBuilder();
		parameterToken.name("X-Authorization")
			.modelRef(new ModelRef("string"))
			.parameterType("header")
			.required(false)
			.build();
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(parameterToken.build());
		return parameters;
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/documentation/**").addResourceLocations("classpath:/META-INF/resources/");
	}
}
