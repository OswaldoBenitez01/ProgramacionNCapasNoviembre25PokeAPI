
package ProgramacionNCapasNoviembre25.PokeAPI.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PokeApiConfig {

    @Bean
    public String pokeApiBaseUrl() {
        return "https://pokeapi.co/api/v2/";
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
