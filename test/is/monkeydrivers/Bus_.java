package is.monkeydrivers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class Bus_ {

    private Bus bus;

    @Before
    public void setUp() throws Exception {
        bus = new SimpleBus();
    }

    @Test
    public void should_send_a_message_to_a_subscriber() throws Exception {
        String type = "foo";
        Message message = createMessageOfType(type);
        Subscriber subscriber = createSubscriberToType(type);

        bus.send(message);
        verify(subscriber).receive(message);
    }

    private Message createMessageOfType(String type) {
        Message message = mock(Message.class);
        doReturn(type).when(message).type();
        return message;
    }

    @Test
    public void should_send_a_message_to_all_subscribers() throws Exception {
        String type = "faa";

        Subscriber subscriber1 = createSubscriberToType(type);
        Subscriber subscriber2 = createSubscriberToType(type);
        Subscriber subscriber3 = createSubscriberToType("foo");

        Message message = createMessageOfType(type);

        bus.send(message);

        verify(subscriber1).receive(message);
        verify(subscriber2).receive(message);
        verify(subscriber3, times(0)).receive(message);
    }

    private Subscriber createSubscriberToType(String type) {
        Subscriber subscriber = mock(Subscriber.class);
        bus.subscribe(subscriber).to(type);
        return subscriber;
    }

    @Test
    public void should_send_a_message_to_subscribers_that_are_not_subscribed() throws Exception {
        Message message = createMessageOfType("faa");

        Subscriber subscriber1 = createSubscriberToType("faa");
        Subscriber subscriber2 = createSubscriberToType("foo");
        Subscriber subscriber3 = createSubscriberToType("foo");


        bus.send(message);

        verify(subscriber1).receive(message);
        verify(subscriber2, times(0)).receive(message);
        verify(subscriber3, times(0)).receive(message);
    }

    @Test
    public void should_send_many_messages_to_subscriber_with_correct_type() throws Exception {
        Subscriber subscriber = createSubscriberToType("foo");

        bus.send(createMessageOfType("faa"));
        bus.send(createMessageOfType("faa"));
        bus.send(createMessageOfType("foo"));
        bus.send(createMessageOfType("foo"));
        bus.send(createMessageOfType("fee"));
        bus.send(createMessageOfType("fee"));

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(subscriber, times(2)).receive(captor.capture());
        assertThat(captor.getAllValues().size(), is(2));
        captor.getAllValues().forEach(m-> assertThat(m.type(), is("foo")));
    }
}
