package autolife.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.consulta.confirmacao}")
    private String confirmacaoQueue;

    @Value("${rabbitmq.queue.consulta.cancelamento}")
    private String cancelamentoQueue;

    @Value("${rabbitmq.queue.consulta.lembrete}")
    private String lembreteQueue;

    @Value("${rabbitmq.queue.cadastro.medico}")
    private String cadastroMedicoQueue;

    @Value("${rabbitmq.queue.cadastro.paciente}")
    private String cadastroPacienteQueue;

    @Bean
    public Queue confirmacaoQueue() {
        return new Queue(confirmacaoQueue, true);
    }

    @Bean
    public Queue cancelamentoQueue() {
        return new Queue(cancelamentoQueue, true);
    }

    @Bean
    public Queue lembreteQueue() {
        return new Queue(lembreteQueue, true);
    }

    @Bean
    public Queue cadastroMedicoQueue() {
        return new Queue(cadastroMedicoQueue, true);
    }

    @Bean
    public Queue cadastroPacienteQueue() {
        return new Queue(cadastroPacienteQueue, true);
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}