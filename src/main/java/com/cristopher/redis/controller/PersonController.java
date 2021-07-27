package com.cristopher.redis.controller;


import com.cristopher.redis.dto.PersonDTO;
import com.cristopher.redis.dto.RangeDTO;
import com.cristopher.redis.service.RedisListCache;
import com.cristopher.redis.service.RedisValueCash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final RedisValueCash valueCache  ;
    private final RedisListCache redisListCache ;

    @Autowired
    public PersonController(final RedisValueCash valueCash , final RedisListCache listCache){
        this.valueCache = valueCash ;
        this.redisListCache  = listCache ;
    }


   @PostMapping
    public void cachePerson(@RequestBody final PersonDTO dto){
        valueCache.cache(dto.getId() , dto);
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable final String id) {
        return (PersonDTO) valueCache.getCachedValue(id);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable final String id){
        valueCache.deleteCachedValue(id);
    }

    //metodos para redis list cache

    @PostMapping("/list/{key}")
    public void cachePersons(@PathVariable final String key , @RequestBody final List<PersonDTO> persons){
        redisListCache.cachePersons(key , persons );

    }
    @GetMapping("/list/{key}")
    public List<PersonDTO> getPersonsInRange(@PathVariable final String key,@RequestBody final RangeDTO range) {
       return redisListCache.getPersonsInRange(key , range);
    }

    @GetMapping("/list/last/{key}")
    public PersonDTO getLastElement(@PathVariable final String key){
        return redisListCache.getLastElement(key);
    }

  @DeleteMapping("/list/{key}")
  public void trim(@PathVariable final String key ,@RequestBody final RangeDTO range){
         redisListCache.trim(key , range);
  }

}
