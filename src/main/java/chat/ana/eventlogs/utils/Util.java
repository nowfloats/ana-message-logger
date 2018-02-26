package chat.ana.eventlogs.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import com.ning.http.util.Base64;


/**
 * Created by tej on 21/10/17.
 */
public class Util {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String getString(JsonNode element) {
        try {
            return mapper.writeValueAsString(element);
        }
        catch(Exception e) {
            return null;
        }
    }

    public static String getBase64EncodedJsonString(JsonNode element) {
        try {
            return Base64.encode(mapper.writeValueAsBytes(element));
        }
        catch(Exception e) {
            return null;
        }
    }

    public static String getAsRequestFormat(String event) {
        return getAsRequestFormat(Arrays.asList(event).iterator());
    }

    public static String getAsRequestFormat(Iterator<String> iter) {
        try {
            ArrayNode arrayNode = mapper.createArrayNode();

            while(iter.hasNext()) {
                String event = iter.next();
                if(event != null) {
                    ObjectNode object =  mapper.createObjectNode();
                    object.put("value", event);
                    arrayNode.add(object);
                }
            }
            ObjectNode output = mapper.createObjectNode();
            output.putPOJO("records", arrayNode);

            return mapper.writeValueAsString(output);
        }
        catch(Exception e) {
            return null;
        }
    }

    public static long getCurrentEpochTimeinUTCinMillis() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
    }

}
