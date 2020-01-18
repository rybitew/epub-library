package pl.app.epublibrary.config;

import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public EpubReader epubReader() {
        return new EpubReader();
    }
}
