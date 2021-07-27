package com.cristopher.redis.service;


import com.cristopher.redis.dto.PersonDTO;
import com.cristopher.redis.dto.RangeDTO;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisListCache {
    private ListOperations<String, Object> listOperations;

    public RedisListCache(RedisTemplate<String, Object> redisTemplate) {
        listOperations = redisTemplate.opsForList();
    }

    public void cachePersons(final String key, final List<PersonDTO> persons) {
        for (final PersonDTO person : persons) {
            listOperations.leftPush(key, person);
        }

        //5 4 3 2 1
    }

    public List<PersonDTO> getPersonsInRange(final String key, final RangeDTO range) {
        final List<Object> objects = listOperations.range(key, range.getFrom(), range.getTo());
        if (CollectionUtils.isEmpty(objects)) {
            return Collections.emptyList();
        }

        return objects.stream()
                .map(x -> (PersonDTO)x)
                .collect(Collectors.toList());
    }

   public PersonDTO getLastElement(final String key){
       final Object o = listOperations.rightPop(key); //right pop ultimo elemento de la lista
        if(o == null ){
            return null ;
        }

        return (PersonDTO) o ;
   }

   public void trim(final String key , final RangeDTO range){
     listOperations.trim(key , range.getFrom() , range.getTo());
   }

}
