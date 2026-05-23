package com.fotoalpha.appointmentsservice.Security;

import com.fotoalpha.appointmentsservice.Entity.Bundles;
import com.fotoalpha.appointmentsservice.Entity.Events;
import com.fotoalpha.appointmentsservice.Enums.BundleSubType;
import com.fotoalpha.appointmentsservice.Enums.BundleType;
import com.fotoalpha.appointmentsservice.Enums.EventName;
import com.fotoalpha.appointmentsservice.Repo.BundleRepo;
import com.fotoalpha.appointmentsservice.Repo.EventRepo;
import com.fotoalpha.appointmentsservice.Security.SecurityObjects.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.sql.SQLOutput;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final BundleRepo bRepo;
    private final EventRepo eRepo;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/admin-api/**").hasRole("ADMIN")
                        .requestMatchers("/test/**").permitAll()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public CommandLineRunner initDatabase(){
        return args -> {
            if (bRepo.count() == 0) {
                System.out.println("---- Creating Bundles ----");
                Bundles GAMMA_bundle = Bundles.builder()
                        .bundleId(1L)
                        .bundlePrice(180000)
                        .bundleType(BundleType.Wedding)
                        .bundleSubType(BundleSubType.GAMMA)
                        .pairLocations(null)
                        .build();
                Bundles BETA_bundle = Bundles.builder()
                        .bundleId(2L)
                        .bundlePrice(222000)
                        .bundleType(BundleType.Wedding)
                        .bundleSubType(BundleSubType.BETA)
                        .pairLocations(null)
                        .build();
                Bundles DELTA_bundle = Bundles.builder()
                        .bundleId(3L)
                        .bundlePrice(280000)
                        .bundleType(BundleType.Wedding)
                        .bundleSubType(BundleSubType.DELTA)
                        .pairLocations(null)
                        .build();
                Bundles ALPHA_bundle = Bundles.builder()
                        .bundleId(4L)
                        .bundlePrice(382000)
                        .bundleType(BundleType.Wedding)
                        .bundleSubType(BundleSubType.ALPHA)
                        .pairLocations(null)
                        .build();
                Bundles ASTERIA_bundle = Bundles.builder()
                        .bundleId(5L)
                        .bundlePrice(45000)
                        .bundleType(BundleType.Pair)
                        .bundleSubType(BundleSubType.ASTERIA)
                        .pairLocations("Budai várnegyed, Halászbástya, Parlamenti nézőpont")
                        .build();
                Bundles AIGIS_bundle = Bundles.builder()
                        .bundleId(6L)
                        .bundlePrice(45000)
                        .bundleType(BundleType.Pair)
                        .bundleSubType(BundleSubType.AIGIS)
                        .pairLocations("Kaputorony, Vajdahunyad vára")
                        .build();
                Bundles MEGARON_bundle = Bundles.builder()
                        .bundleId(7L)
                        .bundlePrice(45000)
                        .bundleType(BundleType.Pair)
                        .bundleSubType(BundleSubType.MEGARON)
                        .pairLocations("Budavári Palota, Várkert bazár")
                        .build();

                bRepo.saveAll(List.of(GAMMA_bundle, BETA_bundle, DELTA_bundle, ALPHA_bundle, ASTERIA_bundle, AIGIS_bundle, MEGARON_bundle));
                System.out.println("---- Bundles Created! ----");

            }
            if (eRepo.count() == 0 ) {
                System.out.println("---- Creating Events ----");
                Events preparation = Events.builder()
                        .eventName(EventName.preparation)
                        .eventPrice(10000)
                        .build();
                Events creativePhoto = Events.builder()
                        .eventName(EventName.creativePhotography)
                        .eventPrice(15000)
                        .build();
                Events ceremony = Events.builder()
                        .eventName(EventName.ceremony)
                        .eventPrice(20000)
                        .build();
                Events dinner = Events.builder()
                        .eventName(EventName.dinner)
                        .eventPrice(15000)
                        .build();
                Events dance = Events.builder()
                        .eventName(EventName.dance)
                        .eventPrice(20000)
                        .build();
                eRepo.saveAll(List.of(preparation, creativePhoto, ceremony, dinner, dance));
                System.out.println("---- Events Created ----");
            }
        };
    }
}
