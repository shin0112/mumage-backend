package mumage.mumagebackend.Config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final CustomFilter customFilter;

    @Autowired
    public SpringConfig(CustomFilter customFilter) {
        this.customFilter = customFilter;
    }

    @Bean
    public FilterRegistrationBean<Filter> firstFilterRegister() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(customFilter);
        return registrationBean;
    }

}