package com.pav.avdonin.functions;

import com.google.gson.*;


import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;


public class StatusButtonsDeserializer  implements JsonDeserializer<StatusButtons> {
    @Override
    public StatusButtons deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        StatusButtons sb = new StatusButtons(jsonObject.get("listofPersons").getAsInt());
        for (int i = 0; i <jsonObject.get("listofPersons").getAsInt(); i++) {
            /*sb.listOfPersons.add()*/
            JsonArray prop = jsonObject.getAsJsonArray("b"+i);
            sb.listOfPersons.add(prop.get(0).getAsString());
            sb.mainButtons[i]=new JButton(); sb.mainButtons[i].setText(prop.get(0).getAsString());

            if(prop.get(1).getAsString().equals("RED")){
                sb.mainButtons[i].setBackground(Color.RED);
            }else sb.mainButtons[i].setBackground(Color.GREEN);

            sb.timeButtons[i]=new JButton();sb.timeButtons[i].setText(prop.get(2).getAsString());
            sb.placeButtons[i]=new JButton(); sb.placeButtons[i].setText(prop.get(3).getAsString());
        }

        return sb;
    }
}