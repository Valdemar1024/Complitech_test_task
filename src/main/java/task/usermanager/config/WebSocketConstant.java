package task.usermanager.config;

public class WebSocketConstant {

    //URL to connect
    public static final String APPLICATION_ENDPOINT = "/user-manager-websocket";
    //Prefix for requests
    public static final String APPLICATION_DESTINATION_PREFIX = "/user-manager";
    //Prefix for responses
    public static final String TOPIC = "/topic";
    public static final String TOPIC_USERS = TOPIC + "/users";
}
