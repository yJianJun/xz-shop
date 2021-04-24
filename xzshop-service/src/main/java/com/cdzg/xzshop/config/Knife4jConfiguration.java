package com.cdzg.xzshop.config;

import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static com.cdzg.xzshop.config.Constant.*;

/**
 * @ClassName : Knife4jConfiguration
 * @Description : Knife4j配置类
 * @Author : XLQ
 * @Date: 2020-11-26 17:39
 * 注意：本地调试请注释所有的 .pathMapping("/xzshop")
 */

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Knife4jConfiguration {

    @Bean
    public Docket createAllRestApi() {
        List<Parameter> paraList = new ArrayList<>();
        ParameterBuilder paraBuilder = new ParameterBuilder();
        springfox.documentation.service.Parameter pc =
                paraBuilder.name(AUTHORIZATION)
                        .description("pc 的Authorization，只有pc的接口才使用.")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build();
        springfox.documentation.service.Parameter mobile =
                paraBuilder.name(MOBILE_AUTHORIZATION)
                        .description("移动端接口token， 只有在使用h5的接口的时候才使用.")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build();
        paraList.add(pc);
        paraList.add(mobile);
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .apiInfo(apiAllInfo())
                .groupName("ALL-所有接口")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiAllInfo() {
        return new ApiInfoBuilder()
                .title("西藏商城业务-全部")
                .description("功能接口")
                .termsOfServiceUrl("http://localhost:8638/")
                .contact(new Contact("nighteer", "http://nighteer.cn", "nighteer@aliyun.com"))
                .version("0.0.1")
                .build();
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

    private static Function<Class<?>, Boolean> annotationPresent(final Class<? extends Annotation> annotation) {
        return new Function<Class<?>, Boolean>() {
            @Override
            public Boolean apply(Class<?> input) {
                return input.isAnnotationPresent(annotation);
            }
        };
    }
    @Bean
    public Docket createPCRestApi() {
        List<Parameter> paraList = new ArrayList<>();
        ParameterBuilder paraBuilder = new ParameterBuilder();
        springfox.documentation.service.Parameter param1 =
                paraBuilder.name(WEB_AUTHORIZATION)
                        .description("pc 的token，只有pc的接口才使用.")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build();
        paraList.add(param1);
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .apiInfo(apiPCInfo())
                .groupName("PC-web接口")
                .select()
                .apis(new Predicate<RequestHandler>() {
                    @Override
                    public boolean apply(RequestHandler input) {
                        return input.isAnnotatedWith(WebApi.class)
                                || declaringClass(input).transform(annotationPresent(WebApi.class)).or(false);
                    }
                })
                .paths(PathSelectors.any())
                .build().globalOperationParameters(paraList);
    }

    private ApiInfo apiPCInfo() {
        return new ApiInfoBuilder()
                .title("西藏业务-web后端功能")
                .description("web后端功能接口")
                .termsOfServiceUrl("http://localhost:8638/")
                .contact(new Contact("nighteer", "http://nighteer.cn", "nighteer@aliyun.com"))
                .version("0.0.1")
                .build();
    }

    @Bean
    public Docket createMobileRestApi() {
        List<Parameter> paraList = new ArrayList<>();
        ParameterBuilder paraBuilder = new ParameterBuilder();
        springfox.documentation.service.Parameter param1 =
                paraBuilder.name(MOBILE_AUTHORIZATION)
                        .description("移动端token， 只有在使用app的接口的时候才使用.")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build();
        paraList.add(param1);
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .apiInfo(apiMobileInfo())
                .groupName("Mobile-移动接口")
                .select()
                .apis(new Predicate<RequestHandler>() {
                    @Override
                    public boolean apply(RequestHandler input) {
                        return input.isAnnotatedWith(MobileApi.class)
                                || declaringClass(input)
                                .transform(annotationPresent(MobileApi.class)).or(false);
                    }
                })
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(paraList);
    }

    private ApiInfo apiMobileInfo() {
        return new ApiInfoBuilder()
                .title("西藏业务-mobile")
                .description("西藏业务移动端功能接口")
                .termsOfServiceUrl("http://localhost:8638/")
                .contact(new Contact("nighteer", "http://nighteer.cn", "nighteer@aliyun.com"))
                .version("0.0.1")
                .build();
    }
}
