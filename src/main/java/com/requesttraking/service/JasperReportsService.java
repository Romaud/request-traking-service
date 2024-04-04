package com.requesttraking.service;

import com.requesttraking.dto.PersonForKafka;
import com.requesttraking.entity.Request;
import com.requesttraking.entity.Role;
import com.requesttraking.entity.User;
import com.requesttraking.entity.common.RoleType;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class JasperReportsService {
    private RequestService requestService;
    private KafkaProducerService kafkaProducer;

    public String exportReport() throws JRException {
//        Request request = requestService.getById(1L);
//        InputStream employeeReportStream = getClass().getResourceAsStream("/request.jrxml");
//        JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(request));
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
//        JasperExportManager.exportReportToHtmlFile(jasperPrint, "C:\\Users\\Mechanic\\Desktop\\request.html");

        PersonForKafka kate = PersonForKafka.builder()
                .id((int) (Math.random() * 1000))
                .username("Kate")
                .errors(List.of())
                .build();

        kafkaProducer.sendMessage(kate);
//        kafkaProducer.sendMessage(MessageBuilder.createMessage(kate, new MessageHeaders(initKafkaHeaders(kate))));

        return "Отчет сгенерирован";
    }

    private Map<String, Object> initKafkaHeaders(PersonForKafka person) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(KafkaHeaders.TOPIC, "request-topic");
        headers.put(KafkaHeaders.MESSAGE_KEY, "1");
        return headers;
    }
}
