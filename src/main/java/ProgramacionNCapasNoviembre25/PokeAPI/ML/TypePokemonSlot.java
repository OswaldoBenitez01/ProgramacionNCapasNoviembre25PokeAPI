
package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TypePokemonSlot {

    @JsonProperty("pokemon")
    private NamedResource pokemon; 
    
    @JsonProperty("slot")
    private Integer slot;

    public NamedResource getPokemon() {
        return pokemon;
    }

    public void setPokemon(NamedResource pokemon) {
        this.pokemon = pokemon;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    
}

