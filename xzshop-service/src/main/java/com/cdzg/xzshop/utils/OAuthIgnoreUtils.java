package com.cdzg.xzshop.utils;

import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.config.annotations.api.WebApi;
import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author org.poem
 */
public class OAuthIgnoreUtils {

    /**
     * 获取数据
     *
     * @param classPath  类上面的RequestMapping路径
     * @param methodPath 方法上面的RequestMapping路径
     * @return
     */
    private static Set<String> getPath(String[] classPath, String[] methodPath) {
        Set<String> path = Sets.newHashSet();
        if (classPath.length > 0 && methodPath.length > 0) {
            Stream.of(classPath).forEach(cl -> {
                Stream.of(methodPath).forEach(me -> {
                    path.add(cl + me);
                });
            });
        } else if (classPath.length > 0 && methodPath.length == 0) {
            path.addAll(Arrays.asList(classPath));
        } else if (classPath.length == 0 && methodPath.length >= 0) {
            path.addAll(Arrays.asList(methodPath));
        }
        return path;
    }

    /**
     * 获取全部的链接，需要
     *
     * @param packageName
     * @return
     */
    public static Set<String> getWebOAuthod(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(RestController.class);
        classesList.addAll(reflections.getTypesAnnotatedWith(Controller.class));

        Set<String> urls = new HashSet<>();
        for (Class classes : classesList) {
            IgnoreAuth classAuthIgnore = (IgnoreAuth) classes.getAnnotation(IgnoreAuth.class);
            if (classAuthIgnore != null) {
                continue;
            }
            String[] name = new String[0];
            RequestMapping requestMapping = (RequestMapping) classes.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                name = requestMapping.value();
            }
            //得到该类下面的所有方法
            Method[] methods = classes.getDeclaredMethods();
            for (Method method : methods) {
                WebApi webApi = method.getAnnotation(WebApi.class);
                IgnoreAuth annotation = method.getAnnotation(IgnoreAuth.class);
                if (null == annotation && webApi != null) {
                    RequestMapping methodRequestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                    if (methodRequestMapping != null) {
                        urls.addAll(getPath(name, methodRequestMapping.value()));
                        continue;
                    }
                    GetMapping methodGetMapping = method.getDeclaredAnnotation(GetMapping.class);
                    if (methodGetMapping != null) {
                        urls.addAll(getPath(name, methodGetMapping.value()));
                        continue;
                    }
                    PostMapping methodPostMapping = method.getDeclaredAnnotation(PostMapping.class);
                    if (methodPostMapping != null) {
                        urls.addAll(getPath(name, methodPostMapping.value()));
                    }
                }
            }
            WebApi webApi = (WebApi) classes.getAnnotation(WebApi.class);
            if (webApi != null) {
                for (Method method : methods) {
                    registeRequestMapping(urls, name, method, webApi != null);
                }
            }
        }
        return urls.stream().map(p -> p.replaceAll("//", "/")).map(
                o -> {
                    if (o.startsWith("/")) {
                        return o.substring(1);
                    }
                    return o;
                }
        ).collect(Collectors.toSet());
    }

    /**
     * 管理路径
     *
     * @param urls
     * @param name
     * @param method
     * @param b
     */
    private static void registeRequestMapping(Set<String> urls, String[] name, Method method, boolean b) {
        IgnoreAuth annotation = method.getAnnotation(IgnoreAuth.class);
        if (null == annotation && b) {
            RequestMapping methodRequestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            if (methodRequestMapping != null) {
                urls.addAll(getPath(name, methodRequestMapping.value()));
                return;
            }
            GetMapping methodGetMapping = method.getDeclaredAnnotation(GetMapping.class);
            if (methodGetMapping != null) {
                urls.addAll(getPath(name, methodGetMapping.value()));
                return;
            }
            PostMapping methodPostMapping = method.getDeclaredAnnotation(PostMapping.class);
            if (methodPostMapping != null) {
                urls.addAll(getPath(name, methodPostMapping.value()));
            }
        }
    }


    /**
     * 获取全部的链接，需要
     *
     * @param packageName
     * @return
     */
    public static Set<String> getMobileOAuthod(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(RestController.class);
        classesList.addAll(reflections.getTypesAnnotatedWith(Controller.class));

        Set<String> urls = new HashSet<>();
        for (Class classes : classesList) {
            IgnoreAuth classAuthIgnore = (IgnoreAuth) classes.getAnnotation(IgnoreAuth.class);
            if (classAuthIgnore != null) {
                continue;
            }
            String[] name = new String[0];
            RequestMapping requestMapping = (RequestMapping) classes.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                name = requestMapping.value();
            }
            //得到该类下面的所有方法
            Method[] methods = classes.getDeclaredMethods();
            for (Method method : methods) {
                //得到该类下面的RequestMapping注解
                MobileApi mobileApi = method.getAnnotation(MobileApi.class);
                registeRequestMapping(urls, name, method, mobileApi != null);
            }
            MobileApi mobileApi = (MobileApi) classes.getAnnotation(MobileApi.class);
            if (mobileApi != null) {
                for (Method method : methods) {
                    registeRequestMapping(urls, name, method, mobileApi != null);
                }
            }
        }

        return urls.stream().map(p -> p.replaceAll("//", "/")).map(
                o -> {
                    if (o.startsWith("/")) {
                        return o.substring(1);
                    }
                    return o;
                }
        ).collect(Collectors.toSet());
    }
}
