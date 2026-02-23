
package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("pokemon")
    private List<TypePokemonSlot> pokemon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypePokemonSlot> getPokemon() {
        return pokemon;
    }

    public void setPokemon(List<TypePokemonSlot> pokemon) {
        this.pokemon = pokemon;
    }

    
    
}

