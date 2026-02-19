package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonCries {
    
    @JsonProperty("legacy")
    private String legacy;
    
    @JsonProperty("latest")
    private String latest;
}
