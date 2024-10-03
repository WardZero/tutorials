package com.baeldung.boot.documentation.springwolf.adapter.incoming;

import com.baeldung.boot.documentation.springwolf.dto.IncomingPayloadDto;
import com.baeldung.boot.documentation.springwolf.service.ProcessorService;
import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.support.mapping.AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME;

@AllArgsConstructor
@Component
@Slf4j
public class IncomingConsumer {

    private static final String TOPIC_NAME = "incoming-topic";

    private final ProcessorService processorService;

    @KafkaListener(topics = TOPIC_NAME)
    @AsyncListener(operation = @AsyncOperation(
            channelName = TOPIC_NAME,
            description = "More details for the incoming topic",
            headers = @AsyncOperation.Headers(
                schemaName = "SpringKafkaDefaultHeadersIncomingPayloadDto",
                values = {
                    // this header is generated by Spring by default
                    @AsyncOperation.Headers.Header(
                        name = DEFAULT_CLASSID_FIELD_NAME,
                        description = "Spring Type Id Header",
                        value = "com.baeldung.boot.documentation.springwolf.dto.IncomingPayloadDto"
                    ),
                }
            )
        )
    )
    @KafkaAsyncOperationBinding
    public void consume(IncomingPayloadDto payload) {
        log.info("Received new message: {}", payload.toString());

        processorService.doHandle(payload);
    }

}
