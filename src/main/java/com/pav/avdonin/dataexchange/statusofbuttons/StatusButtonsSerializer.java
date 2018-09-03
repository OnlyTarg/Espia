package com.pav.avdonin.dataexchange.statusofbuttons;

import com.google.gson.*;

import java.lang.reflect.Type;

public class StatusButtonsSerializer implements JsonSerializer<StatusOfButtons> {

    @Override
    public JsonElement serialize(StatusOfButtons statusOfButtons, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        for (int i = 0; i < statusOfButtons.mainButtons.length; i++) {
            JsonArray jsonArray = new JsonArray();

            result.addProperty("listofPersons", statusOfButtons.listOfPersons.size());
            jsonArray.add(statusOfButtons.mainButtons[i].getText());

            if (statusOfButtons.mainButtons[i].getBackground().getRed() == 255) {
                jsonArray.add("RED");
            } else jsonArray.add("GREEN");

            jsonArray.add(statusOfButtons.timeButtons[i].getText());
            jsonArray.add(statusOfButtons.placeButtons[i].getText());
            result.add("b" + i, jsonArray);
        }

        return result;
    }

}
