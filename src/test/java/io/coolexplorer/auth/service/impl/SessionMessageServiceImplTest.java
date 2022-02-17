package io.coolexplorer.auth.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.coolexplorer.auth.controller.SpringBootWebMvcTestSupport;
import io.coolexplorer.auth.message.JwtTokenMessage;
import io.coolexplorer.auth.message.SessionMessage;
import io.coolexplorer.auth.service.SessionMessageService;
import io.coolexplorer.auth.topic.JwtTokenTopic;
import io.coolexplorer.auth.topic.SessionTopic;
import io.coolexplorer.test.builder.TestAccountBuilder;
import io.coolexplorer.test.builder.TestAuthBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;

@Slf4j
@Tag("embedded-kafka-test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@EmbeddedKafka
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yaml")
public class SessionMessageServiceImplTest extends SpringBootWebMvcTestSupport {
    @Autowired
    private SessionMessageService sessionMessageService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(
            2,
            true,
            2,
            SessionTopic.TOPIC_CREATE_SESSION,
            SessionTopic.TOPIC_UPDATE_SESSION,
            SessionTopic.TOPIC_REQUEST_SESSION,
            SessionTopic.TOPIC_DELETE_SESSION,
            SessionTopic.TOPIC_REPLY_SESSION
    );

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    @BeforeEach
    void setUp() {}

    private void configEmbeddedKafkaConsumer(String topic) {
        Map<String, Object> consumerProperties = new HashMap<>(KafkaTestUtils.consumerProps("session", "false", embeddedKafkaBroker));

        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);

        ContainerProperties containerProperties = new ContainerProperties(topic);
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        records = new LinkedBlockingDeque<>();

        container.setupMessageListener((MessageListener<String, String>) record -> {
            LOGGER.debug("test-listener received message='{}'",
                    record.value());
            records.add(record);
        });
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private Producer<String, Object> configEmbeddedKafkaProducer() {
        Map<String, Object> producerProperties = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        return new DefaultKafkaProducerFactory<>(producerProperties, new StringSerializer(), new JsonSerializer<>()).createProducer();
    }

    @AfterEach
    void tearDown() {
        container.stop();
    }

    @Nested
    @DisplayName("Session Cache Creation Message Test")
    class SessionCacheCreationMessageTest {
        @Test
        @DisplayName("Success")
        void  testCreateMessageForSessionCache() throws InterruptedException, JsonProcessingException {
            configEmbeddedKafkaConsumer(SessionTopic.TOPIC_CREATE_SESSION);

            SessionMessage.CreateMessage createMessage = new SessionMessage.CreateMessage();
            createMessage.setAccountId(TestAccountBuilder.ID);
            createMessage.setValues("test");

            String expectedMessage = objectMapper.writeValueAsString(createMessage);

            sessionMessageService.createSession(createMessage.getAccountId(), createMessage.getValues(), -1L);

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(expectedMessage);
            assertThat(record).has(key(null));
        }
    }

    @Nested
    @DisplayName("Session Cache Update Message Test")
    class SessionCacheUpdateMessageTest {
        @Test
        @DisplayName("Success")
        void testUpdateMessageForSessionCache() throws InterruptedException, JsonProcessingException {
            configEmbeddedKafkaConsumer(SessionTopic.TOPIC_UPDATE_SESSION);

            SessionMessage.UpdateMessage updateMessage = new SessionMessage.UpdateMessage();
            updateMessage.setAccountId(TestAccountBuilder.ID);
            updateMessage.setValues("test");

            String expectedMessage = objectMapper.writeValueAsString(updateMessage);

            sessionMessageService.updateSession(updateMessage.getAccountId(), updateMessage.getValues(), -1L);

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(expectedMessage);
            assertThat(record).has(key(null));
        }
    }

    @Nested
    @DisplayName("Session Cache Request Message Test")
    class SessionCacheRequestMessageTest {
        @Test
        @DisplayName("Success")
        @Disabled("Cannot make the replying message with Embedded kafka")
        void testRequestMessageForSessionCache() throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
            configEmbeddedKafkaConsumer(SessionTopic.TOPIC_REQUEST_SESSION);

            SessionMessage.RequestMessage requestMessage = new SessionMessage.RequestMessage();
            requestMessage.setAccountId(TestAccountBuilder.ID);
            String expectedMessage = objectMapper.writeValueAsString(requestMessage);

            SessionMessage.SessionInfo sessionInfo = new SessionMessage.SessionInfo();
            sessionInfo.setValues("test");
            String replayMessage = objectMapper.writeValueAsString(sessionInfo);

            Producer<String, Object> producer = configEmbeddedKafkaProducer();
            ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(SessionTopic.TOPIC_REPLY_SESSION, replayMessage);
            producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, SessionTopic.TOPIC_REPLY_SESSION.getBytes()));
            producer.send(producerRecord);
            producer.flush();

            sessionMessageService.getSession(requestMessage.getAccountId());

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(expectedMessage);
            assertThat(record).has(key(null));

            producer.close();
        }
    }

    @Nested
    @DisplayName("Session Cache Delete Message Test")
    class SessionCacheDeleteMessageTest {
        @Test
        @DisplayName("Success")
        void testDeleteMessageForSessionCache() throws JsonProcessingException, InterruptedException {
            configEmbeddedKafkaConsumer(SessionTopic.TOPIC_DELETE_SESSION);

            SessionMessage.DeleteMessage deleteMessage = new SessionMessage.DeleteMessage();
            deleteMessage.setAccountId(TestAccountBuilder.ID);
            String expectedMessage = objectMapper.writeValueAsString(deleteMessage);

            sessionMessageService.deleteSession(TestAccountBuilder.ID);

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(expectedMessage);
            assertThat(record).has(key(null));
        }
    }
}
