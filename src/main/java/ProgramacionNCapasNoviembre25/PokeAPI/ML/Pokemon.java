
package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import java.util.List;

public class Pokemon {
    private Integer id;
    private String name;
    private Integer baseExperience;
    private Integer height;
    private Integer weight;
    private Boolean isDefault;
    private Integer order;
    
    private PokemonSprites sprites;
    private List<PokemonType> types;
    private List<PokemonAbility> abilities;
}
