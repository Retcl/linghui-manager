package ltd.jellyfish.modules.authentication.token.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ltd.jellyfish.modules.authentication.token.annotation.UnuseToken;

public class UnuseTokenAnnotationConfig {


    public List<String> getUnusePathList() throws ClassNotFoundException, IOException{
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        String basePackage = "ltd.jellyfish";
        String resourcePath = "/**/*.class";
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(basePackage) + resourcePath;
        Resource[] resources = resourcePatternResolver.getResources(pattern);
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        List<String> pathList = new ArrayList<>();
        for (Resource resource : resources) {
            //用于读取类信息
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            //扫描到的class
            String classname = reader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(classname);
            // 1、判断类中是否存在ClassAnnotation注解，如果存在则打印
            // ClassAnnotation classAnnotation = clazz.getAnnotation(ClassAnnotation.class);            
            RestController restController = clazz.getAnnotation(RestController.class);
            if (restController != null) {
                // 获取属性
                // System.out.println("类中注解——code：" + classAnnotation.code() + "；name：" + classAnnotation.name());
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    UnuseToken useToken = method.getAnnotation(UnuseToken.class);
                    if (useToken != null) {
                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        PutMapping putMapping = method.getAnnotation(PutMapping.class);
                        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        if (postMapping != null) {
                            String paths = postMapping.value()[0];
                            pathList.add(paths);
                        } else if (getMapping != null) {
                            pathList.add(getMapping.value()[0]);
                        } else if (putMapping != null) {
                            pathList.add(putMapping.value()[0]);
                        } else if (deleteMapping != null) {
                            pathList.add(deleteMapping.value()[0]);
                        } else if (requestMapping != null) {
                            pathList.add(requestMapping.value()[0]);
                        } else {
                            throw new ClassNotFoundException();
                        }
                    }
                }
            }
        }
        return pathList;
    }
}
