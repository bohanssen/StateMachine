/**
 *
 */
package me.d2o.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Class: MvcConfiguration
 * 
 * @author bo.hanssen
 * @since Jan 18, 2017 4:49:00 PM
 *
 */
@Configuration
public class MvcConfiguration {

	@Bean
	public WebMvcConfigurer configurer() {
		return new WebMvcConfigurerAdapter() {

			@Autowired
			private EnvironmentInterceptor interceptor;

			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(interceptor);
			}

		};
	}
}
