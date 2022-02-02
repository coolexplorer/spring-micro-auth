package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.JwtTokenMessageService;
import io.coolexplorer.auth.topic.JwtTokenTopic;
import io.coolexplorer.test.builder.TestAccountBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yaml")
public class JwtTokenMessageServiceImplTest {
    @Autowired
    private JwtTokenMessageService jwtTokenMessageService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, JwtTokenTopic.TOPIC_CREATE_JWT_TOKEN);

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    @BeforeEach
    void setUp() {
        embeddedKafka.before();
        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("auth", "false", embeddedKafka.getEmbeddedKafka());

        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
        ContainerProperties containerProperties = new ContainerProperties(JwtTokenTopic.TOPIC_CREATE_JWT_TOKEN);
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        records = new LinkedBlockingDeque<>();

        container.setupMessageListener((MessageListener<String, String>) record -> {
                    LOGGER.debug("test-listener received message='{}'",
                            record.value());
                    records.add(record);
        });
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
    }

    @AfterEach
    void tearDown() {
        container.stop();
        embeddedKafka.after();
    }

    @Nested
    @DisplayName("JwtToken Cache Creation Message Test")
    class JwtTokenCacheCreationMessageTest {
        @Test
        @DisplayName("Success")
        void testCreateMessageForJwtTokenCache() throws InterruptedException {
            Account account = TestAccountBuilder.accountWithToken();

            when(jwtTokenProvider.getExpiredDate(any())).thenReturn(DateUtils.addMinutes(new Date(), 3));

            jwtTokenMessageService.creteJwtTokenCache(account);

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);

            assertThat(record.value()).isEqualTo("test");
            assertThat(record).has(key(null));
        }
    }
}
