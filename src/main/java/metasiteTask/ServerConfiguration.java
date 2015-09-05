package metasiteTask;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author ben
 * @version 1.0
 */
@Configuration
@EnableWebMvc
@ComponentScan
public class ServerConfiguration extends WebMvcAutoConfiguration{
}
