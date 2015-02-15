package com.github.jikuja.myluncher.vanillalauncherprofiles;

import com.github.jikuja.myluncher.vanillalauncherprofiles.AuthenticationDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;


public class AuthenticationDatabaseTest {
    private Gson gson;

    @Before
    public void setup () {
        gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(AuthenticationDatabase.class, new AuthenticationDatabase.Serializer())
                .create();
    }

    @Test
    public void simpleMap () {
        String s = ""
                +"{\"foo\":{"
                    + "\"bar\":\"1\","
                    + "\"baz\":\"2\""
                + "}}";
        AuthenticationDatabase j2n = gson.fromJson(s, AuthenticationDatabase.class);

        Map<String, Map<String, Object>> content = j2n.getContent();
        assertTrue(content.containsKey("foo"));
        assertTrue(content.get("foo").containsKey("bar"));
        assertEquals(content.get("foo").get("bar"), "1");
        assertEquals(gson.toJson(j2n), s);
    }

    @Test
    public void notSoSimpleMap () {
        String s = ""
                +"{\"foo\":{"
                    + "\"bar\":{"
                        + "\"baz\":\"2\""
                + "}}}";
        AuthenticationDatabase j2n = gson.fromJson(s, AuthenticationDatabase.class);

        Map<String, Map<String, Object>> content = j2n.getContent();
        assertTrue(content.containsKey("foo"));
        assertTrue(content.get("foo").containsKey("bar"));
        Map<String, Map<String, Object>> o = (Map<String, Map<String, Object>>)content.get("foo").get("bar");
        assertTrue(o.containsKey("baz"));
        assertEquals(o.get("baz"), "2");
        assertEquals(gson.toJson(j2n), s);
    }

    @Test
    // This is error case which should not happen
    public void badJson() {
        String s = "";
        AuthenticationDatabase j2n = gson.fromJson(s, AuthenticationDatabase.class);
        assertNull(j2n);
    }

    @Test
    public void emptyJson() {
        String s = "{}";
        AuthenticationDatabase j2n = gson.fromJson(s, AuthenticationDatabase.class);
        assertEquals(j2n.getContent().size(), 0);
        assertEquals(gson.toJson(j2n), s);
    }

    @Test
    public void simpleArray() {
        String s = ""
                + "{\"foo\":"
                    + "["
                        + "{\"bar\":\"1\"},"
                        + "{\"baz\":\"2\"}"
                + "]}";
        AuthenticationDatabase j2n = gson.fromJson(s, AuthenticationDatabase.class);

        Map<String, Map<String, Object>> content = j2n.getContent();
        assertTrue(content.containsKey("foo"));
        assertTrue(content.get("foo") instanceof ArrayList);
        assertEquals(((ArrayList)content.get("foo")).size(), 2);
        assertTrue(((ArrayList) content.get("foo")).get(0) instanceof Map);
        assertEquals(((Map<String, Map<String, Object>>)((((ArrayList)content.get("foo")).get(0)))).get("bar"), "1");
    }

    @Test
    public void longTestIsLong() {
        String s = "{\n"
                + "    \"glossary\": {\n"
                + "        \"title\": \"example glossary\",\n"
                + "\t\t\"GlossDiv\": {\n"
                + "            \"title\": \"S\",\n"
                + "\t\t\t\"GlossList\": {\n"
                + "                \"GlossEntry\": {\n"
                + "                    \"ID\": \"SGML\",\n"
                + "\t\t\t\t\t\"SortAs\": \"SGML\",\n"
                + "\t\t\t\t\t\"GlossTerm\": \"Standard Generalized Markup Language\",\n"
                + "\t\t\t\t\t\"Acronym\": \"SGML\",\n"
                + "\t\t\t\t\t\"Abbrev\": \"ISO 8879:1986\",\n"
                + "\t\t\t\t\t\"GlossDef\": {\n"
                + "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n"
                + "\t\t\t\t\t\t\"GlossSeeAlso\": [\"GML\", \"XML\"]\n"
                + "                    },\n"
                + "\t\t\t\t\t\"GlossSee\": \"markup\"\n"
                + "                }\n"
                + "            }\n"
                + "        }\n"
                + "    }\n"
                + "}";

        AuthenticationDatabase j2n = gson.fromJson(s, AuthenticationDatabase.class);
    }

}