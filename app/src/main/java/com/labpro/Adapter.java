package com.labpro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import org.json.XML;
import org.json.JSONArray;
import com.fatboyindustrial.gsonjavatime.LocalDateConverter;
import com.fatboyindustrial.gsonjavatime.LocalDateTimeConverter;
import com.fatboyindustrial.gsonjavatime.LocalTimeConverter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

abstract class Animal  {
    String name;
}

class Dog extends Animal {
    int barkVolume;
}

class Cat extends Animal {
    int livesLeft;
}

class Person {
    String name;
    int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return "Person{name='" + name + "', age=" + age + '}';
    }
}

public class Adapter<T extends ID> {
    private Gson gson;
    private String jsonContent;
    private Class<T> targetClass;

    // Constructor for non-polymorphic class
    public Adapter(String pathToFile, Class<T> targetClass) throws IOException {
        this.gson =  new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
                .create();
        this.targetClass = targetClass;
        this.jsonContent = generateJsonString(pathToFile);
    }

    // Constructor for abstract class/interface
    public Adapter(String pathToFile, Class<T> targetClass, String delimiter,
                   Map<String, Class<? extends T>> mapOfChild) throws IOException {

        RuntimeTypeAdapterFactory<T> adapterFactory =
                RuntimeTypeAdapterFactory.of(targetClass, delimiter);

        for (Map.Entry<String, Class<? extends T>> entry : mapOfChild.entrySet()) {
            adapterFactory.registerSubtype(entry.getValue(), entry.getKey());
        }

        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(adapterFactory)
                .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
                .create();

        this.targetClass = targetClass;
        this.jsonContent = generateJsonString(pathToFile);
    }

    private String generateJsonString(String pathToFile) throws IOException {
        if (pathToFile.endsWith(".json")) {
            return new String(Files.readAllBytes(Paths.get(pathToFile)));
        } else if (pathToFile.endsWith(".xml")) {
            return turnXMLToJson(pathToFile);
        }
        return "";
    }

    private String turnXMLToJson(String pathToFile) throws IOException {
        String xml = new String(Files.readAllBytes(Paths.get(pathToFile)));
        JSONObject jsonObj = XML.toJSONObject(xml);
        String className = targetClass.getSimpleName();

        // get array from root
        JSONObject root = jsonObj.optJSONObject("root");
        if (root != null && root.has(className)) {
            Object Obj = root.get(className);
            if (Obj instanceof org.json.JSONArray) {
                return Obj.toString();
            } else {
                org.json.JSONArray arr = new org.json.JSONArray();
                arr.put(Obj);
                return arr.toString();
            }
        }

        return "[]"; // fallback kosong
    }



    public List<T> parseList() {
        return gson.fromJson(jsonContent, TypeToken.getParameterized(List.class, targetClass).getType());
    }

//    public static void main(String[] args) throws IOException {
////         non-polymorphic
////         Adapter<Person> personAdapter = new Adapter<>("D:\\Codes\\SEMESTER4IF\\Codes\\OOP\\if2010-tubes-2-2425-lah\\app\\src\\main\\resources\\test.json", Person.class);
////         List<Person> people = personAdapter.parseList();
////         System.out.println(people);
//
//        Map<String, Class<? extends Animal>> map = new HashMap<>();
//        map.put("dog", Dog.class);
//        map.put("cat", Cat.class);
//
//        Adapter<Animal> adapter = new Adapter<>("D:\\tubes-oop\\if2010-tubes-2-2425-lah\\app\\src\\main\\java\\com\\labpro\\tesabsxml.xml", Animal.class, "type", map);
//        List<Animal> animals = adapter.parseList();
//
//        for (Animal a : animals) {
//            System.out.println(a.getClass().getSimpleName() + ": " + a.name);
//        }
//    }
}


// format XML yang bisa
// <root>
//   <Animal>...</Animal>
//   <Animal>...</Animal>
// </root>
// untuk XML tag setelah root wajib nama dari kelas kita in memory

// format json yang bisa
// [
//   {"name": "Bob", "age": 25},
//   {"name": "Charlie", "age": 35},
//   {"name": "Diana", "age": 40}
// ]

// jika polymorphic
// [
//   { "type": "dog", "name": "Buddy", "barkVolume": 5 },
//   { "type": "cat", "name": "Whiskers", "livesLeft": 9 }
// ]