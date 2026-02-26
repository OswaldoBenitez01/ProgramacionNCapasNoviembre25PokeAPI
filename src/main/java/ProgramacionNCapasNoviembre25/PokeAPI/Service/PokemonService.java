package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.ML.Pokemon;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.PokemonListResponse;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.TypePokemonSlot;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.TypeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    private final RestTemplate restTemplate;
    private final String pokeApiBaseUrl;

    public PokemonService(RestTemplate restTemplate, String pokeApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.pokeApiBaseUrl = pokeApiBaseUrl;
    }

   public Result getPokemonFiltered(String type, String region, int limit, int offset) {
       Result result = new Result();

       try {
           List<Pokemon> listaFiltrada = new ArrayList<>();

           boolean hayTipo   = type   != null && !type.trim().isEmpty();
           boolean hayRegion = region != null && !region.trim().isEmpty();

           if (hayTipo && hayRegion) {
               String urlTipo = pokeApiBaseUrl + "type/" + type.toLowerCase();
               TypeResponse typeResponse = restTemplate.getForObject(urlTipo, TypeResponse.class);

               int[] rango = getRegionRange(region);

               List<TypePokemonSlot> filtradosPorRegion = typeResponse.getPokemon()
                       .stream()
                       .filter(slot -> {
                           int id = extraerIdDesdeUrl(slot.getPokemon().getUrl());
                           return id >= rango[0] && id <= rango[1];
                       })
                       .collect(Collectors.toList());

               int inicio  = Math.min(offset, filtradosPorRegion.size());
               int fin     = Math.min(offset + limit, filtradosPorRegion.size());
               List<TypePokemonSlot> pagina = filtradosPorRegion.subList(inicio, fin);

               List<CompletableFuture<Pokemon>> hilos = pagina.stream()
                       .map(slot -> CompletableFuture.supplyAsync(() -> {
                           String urlDetalle = pokeApiBaseUrl + "pokemon/" + slot.getPokemon().getName();
                           return restTemplate.getForObject(urlDetalle, Pokemon.class);
                       }))
                       .collect(Collectors.toList());

               for (CompletableFuture<Pokemon> hilo : hilos) {
                   Pokemon p = hilo.get();
                   if (p != null) listaFiltrada.add(p);
               }

               result.Object = filtradosPorRegion.size();

           } else if (hayTipo) {
               return getPokemonByType(type, limit, offset);
           } else if (hayRegion) {
               return getPokemonByRegion(region, limit, offset);
           } else {
               return getPokemonList(limit, offset);
           }

           result.Correct = true;
           result.Objects = new ArrayList<>(listaFiltrada);

       } catch (Exception ex) {
           result.Correct = false;
           result.ErrorMessage = "Error al filtrar: " + ex.getMessage();
           result.ex = ex;
       }

       return result;
   }

   private int extraerIdDesdeUrl(String url) {
       try {
           String sin_slash = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
           String idStr = sin_slash.substring(sin_slash.lastIndexOf("/") + 1);
           return Integer.parseInt(idStr);
       } catch (Exception e) {
           return -1;
       }
   }


    public Result getPokemonList(int limit, int offset) {
        Result result = new Result();
        
        try {
            String urlBasic = pokeApiBaseUrl + "pokemon?limit=" + limit + "&offset=" + offset;
            PokemonListResponse basicList = restTemplate.getForObject(urlBasic, PokemonListResponse.class);
            
            List<Pokemon> pokemonsCompletos = new ArrayList<>();
            
            List<CompletableFuture<Pokemon>> listaHilos = basicList.getResults().stream()
                .map(pokeBasic -> CompletableFuture.supplyAsync(() -> {
                    String urlCompleto = pokeApiBaseUrl + "pokemon/" + pokeBasic.getName();
                    return restTemplate.getForObject(urlCompleto, Pokemon.class);
                }))
                .collect(Collectors.toList());

            for (CompletableFuture<Pokemon> hilo : listaHilos) {
                Pokemon pokemonCompleto = hilo.get();
                if (pokemonCompleto != null) {
                    pokemonsCompletos.add(pokemonCompleto);
                }
            }

            result.Correct = true;
            result.Objects = new ArrayList<>(pokemonsCompletos);
            result.Object = "nextOffset=" + (offset + limit);
            result.ErrorMessage = basicList.getCount().toString();
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "Error lista completa: " + ex.getMessage();
            result.ex = ex;
        }
        
        return result;
    }

    public Result getPokemonByIdOrName(String idOrName) {
        Result result = new Result();
        try {
            String url = pokeApiBaseUrl + "pokemon/" + idOrName;
            Pokemon pokemon = restTemplate.getForObject(url, Pokemon.class);
            
            result.Correct = true;
            result.Object = pokemon;
            
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "No encontrado: " + idOrName;
            result.ex = ex;
        }
        return result;
    }
    
    public Result getAllTypes() {
        Result result = new Result();
        try {
            String url = pokeApiBaseUrl + "type?limit=100";
            PokemonListResponse response = restTemplate.getForObject(url, PokemonListResponse.class);

            result.Correct = true;
            result.Objects = new ArrayList<>(response.getResults());
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "Error al obtener tipos: " + ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    public Result getPokemonByType(String typeName, int limit, int offset) {
        Result result = new Result();
        try {
            String url = pokeApiBaseUrl + "type/" + typeName.toLowerCase();
            TypeResponse typeResponse = restTemplate.getForObject(url, TypeResponse.class);

            if (typeResponse == null || typeResponse.getPokemon() == null) {
                result.Correct = false;
                result.ErrorMessage = "No se encontraron pokémon del tipo: " + typeName;
                return result;
            }

            List<TypePokemonSlot> todos = typeResponse.getPokemon();
            int inicio = Math.min(offset, todos.size());
            int fin    = Math.min(offset + limit, todos.size());
            List<TypePokemonSlot> pagina = todos.subList(inicio, fin);

            List<CompletableFuture<Pokemon>> hilos = pagina.stream()
                    .map(slot -> CompletableFuture.supplyAsync(() -> {
                        String urlDetalle = pokeApiBaseUrl + "pokemon/" + slot.getPokemon().getName();
                        return restTemplate.getForObject(urlDetalle, Pokemon.class);
                    }))
                    .collect(Collectors.toList());

            List<Pokemon> pokemonsCompletos = new ArrayList<>();
            for (CompletableFuture<Pokemon> hilo : hilos) {
                Pokemon p = hilo.get();
                if (p != null) {
                    pokemonsCompletos.add(p);
                }
            }

            result.Correct = true;
            result.Objects = new ArrayList<>(pokemonsCompletos);
            result.Object  = todos.size();

        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "Error filtrando por tipo: " + ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    public Result getPokemonByRegion(String regionName, int limit, int offset) {
        Result result = new Result();
        try {
            int[] rango = getRegionRange(regionName);
            if (rango == null) {
                result.Correct = false;
                result.ErrorMessage = "Región no reconocida: " + regionName;
                return result;
            }

            int idInicio = rango[0];
            int idFin    = rango[1];
            int total    = idFin - idInicio + 1;

            int inicio = idInicio + offset;
            int fin    = Math.min(inicio + limit, idFin + 1);

            List<Integer> ids = new ArrayList<>();
            for (int i = inicio; i < fin; i++) {
                ids.add(i);
            }

            List<CompletableFuture<Pokemon>> hilos = ids.stream()
                    .map(id -> CompletableFuture.supplyAsync(() -> {
                        String urlDetalle = pokeApiBaseUrl + "pokemon/" + id;
                        return restTemplate.getForObject(urlDetalle, Pokemon.class);
                    }))
                    .collect(Collectors.toList());

            List<Pokemon> pokemonsCompletos = new ArrayList<>();
            for (CompletableFuture<Pokemon> hilo : hilos) {
                Pokemon p = hilo.get();
                if (p != null) {
                    pokemonsCompletos.add(p);
                }
            }

            result.Correct = true;
            result.Objects = new ArrayList<>(pokemonsCompletos);
            result.Object  = total;

        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "Error filtrando por región: " + ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    public int[] getRegionRange(String region) {
        if (region == null || region.isBlank()) return null;
        switch (region.toLowerCase()) {
            case "kanto":  return new int[]{1,   151};
            case "johto":  return new int[]{152, 251};
            case "hoenn":  return new int[]{252, 386};
            case "sinnoh": return new int[]{387, 493};
            case "unova":  return new int[]{494, 649};
            case "kalos":  return new int[]{650, 721};
            case "alola":  return new int[]{722, 809};
            case "galar":  return new int[]{810, 898};
            default:       return null;
        }
    }

}
