package com.alibaba.dubbo.javaconfig.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.javaconfig.annotation.Dubbo;
import com.alibaba.dubbo.javaconfig.annotation.DubboDeploy;

public class DubboScannerAnnotationClass {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	public List<Class> scan(String annotationPackage) {
		try {
			return findMyTypes(annotationPackage);
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
		return new ArrayList<Class>();
	}

	private List<Class> findMyTypes(String basePackage) throws IOException, ClassNotFoundException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		List<Class> candidates = new ArrayList<Class>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage)
				+ "/" + "**/*.class";
		Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
		for (Resource resource : resources) {
			logger.debug(resource.getFilename() + " readable : " + resource.isReadable());
			if (resource.isReadable()) {
				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				logger.debug(resource.getFilename() + " candidate : " + isCandidate(metadataReader));
				if (isCandidate(metadataReader)) {
					candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
				}
			}
		}
		return candidates;
	}

	private String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

	private boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException {
		try {
			Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
			if (c.getAnnotation(Dubbo.class) != null || c.getAnnotation(DubboDeploy.class) != null) {
				return true;
			}
		} catch (Throwable e) {
		}
		return false;
	}

}
