package com.pav.avdonin.functions;

import com.google.gson.*;

import java.lang.reflect.Type;

public class StatusButtonsSerializer implements JsonSerializer<StatusButtons> {

        @Override
        public JsonElement serialize(StatusButtons statusButtons, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject result = new JsonObject();

            for (int i = 0; i <statusButtons.mainButtons.length ; i++) {
                JsonArray jsonArray = new JsonArray();

                result.addProperty("listofPersons",statusButtons.listOfPersons.size());
                jsonArray.add(statusButtons.mainButtons[i].getText());

                if(statusButtons.mainButtons[i].getBackground().getRed()==255){
                    jsonArray.add("RED");
                }else jsonArray.add("GREEN");

                jsonArray.add(statusButtons.timeButtons[i].getText());
                jsonArray.add(statusButtons.placeButtons[i].getText());
                result.add("b"+i,jsonArray);
                }

            return result;
        }

}
