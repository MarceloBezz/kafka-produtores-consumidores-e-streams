package br.com.alura;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {
    public static void main(String[] args) {
        var emailService = new EmailService();
        var service = new KafkaService<Email>(EmailService.class.getSimpleName(), "ECOMMERCE_SEND_EMAIL",
                emailService::parse,
                Map.of());
        service.run();
    }

    private void parse(ConsumerRecord<String, Message<Email>> record) {
        System.out.println("------------------------------------------");
        System.out.println("SEND EMAIL");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        System.out.println("Email sent!");
    }
}
