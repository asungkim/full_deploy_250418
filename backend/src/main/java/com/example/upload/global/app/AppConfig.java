package com.example.upload.global.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AppConfig {

    @Getter
    public static ObjectMapper objectMapper;

    @Getter
    public static String genFileDirPath;

    private static Environment environment;

    @Getter
    private static String siteBackUrl;

    @Value("${custom.site.backUrl}")
    public void setSiteBackUrl(String siteBackUrl) {
        AppConfig.siteBackUrl = siteBackUrl;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        AppConfig.environment = environment;
    }

    public static boolean isProd() {
        return environment.matchesProfiles("prod");
    }

    public static boolean isDev() {
        return environment.matchesProfiles("dev");
    }

    public static boolean isTest() {
        return environment.matchesProfiles("test");
    }

    @Value("${custom.genFile.dirPath}")
    public void setGenFileDirPath(String genFileDirPath) {
        AppConfig.genFileDirPath = genFileDirPath;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        AppConfig.objectMapper = objectMapper;
    }

    public static String getSiteFrontUrl() {
        return "http://localhost:3000";
    }

    public static boolean isNotProd() {
        return true;
    }

    public static String getTempDirPath() {
        return System.getProperty("java.io.tmpdir");
    }

    @Getter
    private static Tika tika;

    @Autowired
    public void setTika(Tika tika) {
        AppConfig.tika = tika;
    }

    private static String resourcesSampleDirPath;

    @SneakyThrows
    public static String getResourcesSampleDirPath() {
        if (resourcesSampleDirPath == null) {
            ClassPathResource resource = new ClassPathResource("sample");

            if (resource.exists()) {
                try {
                    // 파일 시스템 상에 존재하는 경우
                    resourcesSampleDirPath = resource.getFile().getAbsolutePath();
                } catch (IllegalStateException | java.io.FileNotFoundException e) {
                    // JAR 내부에 존재하는 경우 -> 임시 디렉토리로 복사
                    java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("sample-copy");
                    java.nio.file.Path tempFile = tempDir.resolve("sample");

                    try (java.io.InputStream is = resource.getInputStream()) {
                        java.nio.file.Files.copy(is, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }

                    resourcesSampleDirPath = tempDir.toAbsolutePath().toString();
                }
            } else {
                // fallback
                resourcesSampleDirPath = "src/main/resources/sample";
            }
        }

        return resourcesSampleDirPath;
    }

    @Getter
    private static String springServletMultipartMaxFileSize;

    @Value("${spring.servlet.multipart.max-file-size}")
    public void setSpringServletMultipartMaxFileSize(String springServletMultipartMaxFileSize) {
        this.springServletMultipartMaxFileSize = springServletMultipartMaxFileSize;
    }

    @Getter
    private static String springServletMultipartMaxRequestSize;

    @Value("${spring.servlet.multipart.max-request-size}")
    public void setSpringServletMultipartMaxRequestSize(String springServletMultipartMaxRequestSize) {
        this.springServletMultipartMaxRequestSize = springServletMultipartMaxRequestSize;
    }

}
