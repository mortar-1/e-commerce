package mortar.euroshopper.eCommerceApplication;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@Profile("dev")
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        proxyTargetClass = true,
        prePostEnabled = true,
        jsr250Enabled = true)
@EnableWebSecurity
public class SecurityConfigurationDevelopment extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //H2-console
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
                .antMatchers("/h2", "/h2/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/shop").permitAll()
                .antMatchers("/cart").permitAll()
                .antMatchers("/items/{id}").permitAll()
                .antMatchers("/items").hasAuthority("ADMIN")
                .antMatchers("/items/**").hasAuthority("ADMIN")
                .antMatchers("/accounts").hasAuthority("ADMIN")
                .antMatchers("/accounts/**").hasAuthority("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/shop?logout=true");

        http.formLogin().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
