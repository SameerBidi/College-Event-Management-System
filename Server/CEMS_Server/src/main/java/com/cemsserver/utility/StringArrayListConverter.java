package com.cemsserver.utility;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Converter
public class StringArrayListConverter implements AttributeConverter<ArrayList<String>, String> 
{
    Gson gson = new Gson();

    @Override
    public String convertToDatabaseColumn(ArrayList<String> stringList) 
    {
        return gson.toJson(stringList);
    }

    @Override
    public ArrayList<String> convertToEntityAttribute(String string) 
    {
        return gson.fromJson(string, new TypeToken<ArrayList<String>>(){}.getType());
    }
}