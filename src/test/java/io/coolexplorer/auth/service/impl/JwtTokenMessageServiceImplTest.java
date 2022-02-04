package io.coolexplorer.auth.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.coolexplorer.auth.controller.SpringBootWebMvcTestSupport;
import io.coolexplorer.auth.message.JwtTokenMessage;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.JwtTokenMessageService;
import io.coolexplorer.auth.topic.JwtTokenTopic;
import io.coolexplorer.test.builder.TestAccountBuilder;
import io.coolexplorer.test.builder.TestAuthBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@EmbeddedKafka
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yaml")
public class JwtTokenMessageServiceImplTest extends SpringBootWebMvcTestSupport {
    @Autowired
    private JwtTokenMessageService jwtTokenMessageService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(
            2,
            true,
            2,
            JwtTokenTopic.TOPIC_CREATE_JWT_TOKEN,
            JwtTokenTopic.TOPIC_REQUEST_JWT_TOKEN,
            JwtTokenTopic.TOPIC_UPDATE_JWT_TOKEN,
            JwtTokenTopic.TOPIC_DELETE_JWT_TOKEN,
            JwtTokenTopic.TOPIC_REPLY_JWT_TOKEN
    );

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    @BeforeEach
    void setUp() {}

    private void configEmbeddedKafkaConsumer(String topic) {
        Map<String, Object> consumerProperties = new HashMap<>(KafkaTestUtils.consumerProps("auth", "false", embeddedKafkaBroker));

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
    @DisplayName("JwtToken Cache Creation Message Test")
    class JwtTokenCacheCreationMessageTest {
        @Test
        @DisplayName("Success")
        void testCreateMessageForJwtTokenCache() throws InterruptedException, JsonProcessingException {
            configEmbeddedKafkaConsumer(JwtTokenTopic.TOPIC_CREATE_JWT_TOKEN);

            Account account = TestAccountBuilder.accountWithToken();

            JwtTokenMessage.CreateMessage createMessage = JwtTokenMessage.CreateMessage.from(account, null);
            String expectedMessage = objectMapper.writeValueAsString(createMessage);

            when(jwtTokenProvider.getExpiredDate(any())).thenReturn(null);

            jwtTokenMessageService.creteJwtTokenCache(account);

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(expectedMessage);
            assertThat(record).has(key(null));
        }
    }

    @Nested
    @DisplayName("JwtToken Cache Update Message Test")
    class JwtTokenCacheUpdateMessageTest {
        @Test
        @DisplayName("Success")
        void testUpdateMessageForJwtTokenCache() throws JsonProcessingException, InterruptedException {
            configEmbeddedKafkaConsumer(JwtTokenTopic.TOPIC_UPDATE_JWT_TOKEN);

            Account account = TestAccountBuilder.accountWithToken();

            JwtTokenMessage.UpdateMessage updateMessage = JwtTokenMessage.UpdateMessage.from(account, null);
            String expectedMessage = objectMapper.writeValueAsString(updateMessage);

            when(jwtTokenProvider.getExpiredDate(any())).thenReturn(null);

            jwtTokenMessageService.updateJwtTokenCache(account);

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(expectedMessage);
            assertThat(record).has(key(null));
        }
    }

    @Nested
    @DisplayName("JwtToken Cache Request Message Test")
    class JwtTokenCacheRequestMessageTest {
        @Test
        @DisplayName("Success")
        @Disabled("Cannot make the replying message with Embedded kafka")
        void testRequestMessageForJwtTokenCache() throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
            configEmbeddedKafkaConsumer(JwtTokenTopic.TOPIC_REQUEST_JWT_TOKEN);

            JwtTokenMessage.RequestMessage requestMessage = new JwtTokenMessage.RequestMessage();
            requestMessage.setAccountId(TestAccountBuilder.ID);
            String expectedMessage = objectMapper.writeValueAsString(requestMessage);

            JwtTokenMessage.JwtTokenInfo jwtTokenInfo = new JwtTokenMessage.JwtTokenInfo();
            jwtTokenInfo.setJwtToken(TestAuthBuilder.TOKEN);
            String replayMessage = objectMapper.writeValueAsString(jwtTokenInfo);

            Producer<String, Object> producer = configEmbeddedKafkaProducer();
            ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(JwtTokenTopic.TOPIC_REPLY_JWT_TOKEN, replayMessage);
            producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, JwtTokenTopic.TOPIC_REPLY_JWT_TOKEN.getBytes()));
            producer.send(producerRecord);
            producer.flush();

            jwtTokenMessageService.getJwtTokenCache(requestMessage);

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(expectedMessage);
            assertThat(record).has(key(null));

            producer.close();
        }
    }

    @Nested
    @DisplayName("JwtToken Cache Delete Message Test")
    class JwtTokenCacheDeleteMessageTest {
        @Test
        @DisplayName("Success")
        void testDeleteMessageForJwtTokenCache() throws JsonProcessingException, InterruptedException {
            configEmbeddedKafkaConsumer(JwtTokenTopic.TOPIC_DELETE_JWT_TOKEN);

            JwtTokenMessage.DeleteMessage deleteMessage = new JwtTokenMessage.DeleteMessage();
            deleteMessage.setAccountId(TestAccountBuilder.ID);
            String expectedMessage = objectMapper.writeValueAsString(deleteMessage);

            jwtTokenMessageService.deleteJwtTokenCache(TestAccountBuilder.ID);

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(expectedMessage);
            assertThat(record).has(key(null));
        }
    }
}
