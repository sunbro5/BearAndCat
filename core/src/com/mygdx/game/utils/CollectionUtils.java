package com.mygdx.game.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

    public static <T> Set<T> setOf(T... objects){
        Set<T> set = new HashSet<>();
        if(objects == null){
            return set;
        }
        for (T object: objects) {
            set.add(object);
        }
        return set;
    }

    public static <T> List<T> listOf(T... objects){
        List<T> set = new ArrayList<>();
        if(objects == null){
            return set;
        }
        for (T object: objects) {
            set.add(object);
        }
        return set;
    }

    public static <T> T randomObject(Collection<T> coll) {
        if(coll == null || coll.size() < 1){
            return null;
        }
        int num = (int) (Math.random() * coll.size());
        for(T t: coll) if (--num < 0) return t;
        return null;
    }

}
