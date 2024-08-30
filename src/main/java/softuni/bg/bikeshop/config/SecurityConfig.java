package softuni.bg.bikeshop.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import softuni.bg.bikeshop.repository.UserRepository;
import softuni.bg.bikeshop.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
         httpSecurity
                 .authorizeHttpRequests(
                        authorizeRequests -> {
                            authorizeRequests
                                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                    .requestMatchers("/", "/about","/products", "/products/details/**").permitAll()
                                    .requestMatchers("/users/login", "/users/register","/login-error").anonymous()


                                    .requestMatchers("/admin/**","/products/add-bike", "/edit-bike/**",
                                            "/products/add-part", "/edit-part",
                                            "/products/add", "/products/delete/**",
                                            "/products/product-management", "/products/edit/**",
                                            "/products/edit-part").hasRole("ADMIN")


                                    .requestMatchers("/products/add-to-favourites/**", "/products/favourites",
                                            "/products/remove-from-favourites/**", "/products/buy/**",
                                            "/products/add-to-bag/**", "/user/my-bag",
                                            "/order/update-quantities", "/order/remove/**",
                                            "/order/delivery-details", "/order/submit-delivery-details",
                                            "/order/load-delivery-details", "/result",
                                            "/user/orders").hasRole("USER")


                                    .anyRequest().authenticated();
                        })
                .formLogin(
                        formLogin -> {
                            formLogin
                                    .loginPage("/users/login")
                                    .usernameParameter("username")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/",true)
                                    .failureForwardUrl("/login-error");
                        }
                )
                .logout(
                        logout -> {
                            logout
                                    .logoutUrl("/users/logout")
                                    .logoutSuccessUrl("/")
                                    .invalidateHttpSession(true);
                        }
                );
            httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/products/add-part"));
            httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/api/create-checkout-session"));
             httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/edit-part/**")));

        return httpSecurity.build();

    }
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return new UserDetailsServiceImpl(userRepository);
    }
}
