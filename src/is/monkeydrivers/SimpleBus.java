package is.monkeydrivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bartlomiej on 29.11.2016.
 */
class SimpleBus implements Bus {
    private Map<String, List<Subscriber>> subscribers = new HashMap<>();

    private List<Subscriber> subscribersOf(String type) {
        createSubscribersListIfNotExist(type);
        return subscribers.get(type);
    }

    private void createSubscribersListIfNotExist(String type) {
        if (!subscribers.containsKey(type)) subscribers.put(type, new ArrayList<>());
    }

    @Override
    public Subscription subscribe(Subscriber subscriber) {
        return type -> subscribersOf(type).add(subscriber);
    }

    @Override
    public void send(Message message) {
        subscribersOf(message.type()).forEach(s -> s.receive(message));
    }
}
