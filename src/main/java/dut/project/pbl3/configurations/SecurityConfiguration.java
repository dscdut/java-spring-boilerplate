package dut.project.pbl3.configurations;

import dut.project.pbl3.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomizeLogoutSuccessHandler customizeLogoutSuccessHandler;
    private final CustomizeAuthenticationFailureHandler customizeAuthenticationFailureHandler;
    private final CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }

    @Override
    public void configure(WebSecurity web){
        web
            .ignoring()
            .antMatchers("/resources/**", "/static/**",
                    "/css/**", "/js/**", "/images/**", "/vendor/**","/template/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .anyRequest()
            .authenticated();
        http
            .formLogin()
            .loginPage("/login").permitAll()
                .successHandler(customizeAuthenticationSuccessHandler)
                .failureHandler(customizeAuthenticationFailureHandler)
                .usernameParameter("email")
                .passwordParameter("password");
        http
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(customizeLogoutSuccessHandler);
        http
            .authorizeRequests()
            .and()
            .exceptionHandling()
            .accessDeniedPage("/forbidden");
    }

}
