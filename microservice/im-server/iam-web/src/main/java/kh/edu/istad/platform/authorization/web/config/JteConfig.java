package kh.edu.istad.platform.authorization.web.config;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.resolve.ResourceCodeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Configuration
public class JteConfig {

    private static final Logger log = LoggerFactory.getLogger(JteConfig.class);

    @Bean(name = {"templateEngine", "jteTemplateEngine"})
    @ConditionalOnMissingBean(TemplateEngine.class)
    public TemplateEngine jteTemplateEngine() {
        List<Path> candidatePaths = List.of(
                Path.of("microservice/im-server/iam-web/src/main/jte"),
                Path.of("iam-web/src/main/jte"),
                Path.of("src/main/jte"),
                Path.of("microservice/im-server/iam-web/src/main/resources/jte"),
                Path.of("iam-web/src/main/resources/jte")
        );

        CodeResolver codeResolver = null;
        for (Path candidate : candidatePaths) {
            if (Files.isDirectory(candidate)) {
                log.info("JTE resolved template directory at: {}", candidate.toAbsolutePath());
                codeResolver = new DirectoryCodeResolver(candidate);
                break;
            }
        }

        if (codeResolver == null) {
            log.info("JTE falling back to ResourceCodeResolver(jte)");
            codeResolver = new ResourceCodeResolver("jte");
        }

        Path classDirectory = Path.of("build/jte-classes");
        return TemplateEngine.create(codeResolver, classDirectory, ContentType.Html, getClass().getClassLoader());
    }
}

