package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonStatSlot {
    
    @JsonProperty("base_stat")
    private int baseStat;
    
    @JsonProperty("effort")
    private int effort;
    
    @JsonProperty("stat")
    private NamedResource stat;
}
