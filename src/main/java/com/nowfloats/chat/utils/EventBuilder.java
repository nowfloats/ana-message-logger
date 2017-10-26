package com.nowfloats.chat.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tej on 21/10/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "event_name",
        "event_section",
        "epoch_time",
        "properties",
})
public class EventBuilder {
    @JsonProperty("event_name")
    private String eventName;

    public String getEventName() {
        return eventName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getEventSection() {
        return eventSection;
    }

    public Long getEpochTime() {
        return epochTime;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("event_section")
    private String eventSection;

    @JsonProperty("epoch_time")
    private Long epochTime;

    @JsonProperty("properties")
    private Map<String, Object> properties = new LinkedHashMap<String, Object>();


    @JsonIgnore
    private String timeFieldKey;

    public EventBuilder(){}

    public EventBuilder(String eventName, String eventSection, Long epochTimeUtcInSeconds) {
        this.eventName = eventName;
        this.eventSection = eventSection;
        this.epochTime = epochTimeUtcInSeconds;
    }

    public void setTimeFieldKey(String timeFieldKey) {
        this.timeFieldKey = timeFieldKey;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }

    public void addProperty(String key, Object value) {
        if(this.properties == null) {
            this.properties = new HashMap<String, Object>();
        }
        this.properties.put(key, value);
    }

    public void addAllProperties(String key, Object value) {
        if(this.properties == null) {
            this.properties = new HashMap<String, Object>();
        }
        this.properties.put(key, value);
    }

    public JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(this);
    }

}
