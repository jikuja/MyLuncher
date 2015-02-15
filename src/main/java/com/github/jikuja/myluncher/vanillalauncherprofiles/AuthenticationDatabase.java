package com.github.jikuja.myluncher.vanillalauncherprofiles;

import com.google.gson.*;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.*;

public class AuthenticationDatabase  {
    @Getter
    private Map<String, Map<String, Object>> content;

    public AuthenticationDatabase (Map<String, Map<String, Object>> a) {
        this.content = a;
    }

    public String getIdByUserName(String name) {
        for (Map.Entry<String, Map <String, Object>> e : getContent().entrySet()) {
            if (e.getValue().get("username").equals(name)) {
                return e.getKey();
            }
        }
        return null;
    }

    public Map<String, Object> getByUserName(String name) {
        for (Map.Entry<String, Map <String, Object>> e : getContent().entrySet()) {
            if (e.getValue().get("username").equals(name)) {
                return e.getValue();
            }
        }
        return null;
    }

    public void setByUserName(String user, Map<String, Object> o) {
        getContent().put(getIdByUserName(user), o);
    }

    /**
     * GSON inheritance problem. 1:1 copy from Json2Neste. Fix later
     */
    public static class Serializer implements JsonSerializer<AuthenticationDatabase>, JsonDeserializer<AuthenticationDatabase> {
        public AuthenticationDatabase deserialize (JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Map<String, Map<String, Object>> h = (Map<String, Map<String, Object>>) deserializeToNested(json);
            return new AuthenticationDatabase(h);
        }

        private Object deserializeToNested (JsonElement el) {
            if ((el instanceof JsonObject)) {
                Map<String, Object> result = new LinkedHashMap();
                for (Map.Entry<String, JsonElement> entry : ((JsonObject) el).entrySet()) {
                    result.put(entry.getKey(), deserializeToNested(entry.getValue()));
                }
                return result;
            } else if ((el instanceof JsonArray)) {
                List<Object> result = new ArrayList();
                for (JsonElement entry : (JsonArray) el) {
                    result.add(deserializeToNested(entry));
                }
                return result;
            } else {
                //JsonPrimitive or JsonNull
                return el.getAsString();
            }
        }

        public JsonElement serialize (AuthenticationDatabase src, Type typeOfSrc, JsonSerializationContext context) {
            Map<String, Map<String, Object>> services = src.getContent();
            return context.serialize(services);
        }
    }
}
