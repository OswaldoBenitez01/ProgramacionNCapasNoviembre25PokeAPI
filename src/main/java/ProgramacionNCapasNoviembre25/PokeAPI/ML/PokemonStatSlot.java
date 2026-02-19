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

    public int getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(int baseStat) {
        this.baseStat = baseStat;
    }

    public int getEffort() {
        return effort;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public NamedResource getStat() {
        return stat;
    }

    public void setStat(NamedResource stat) {
        this.stat = stat;
    }
    
    
}
