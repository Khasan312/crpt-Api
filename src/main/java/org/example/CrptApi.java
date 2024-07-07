package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class CrptApi {
    private static final Logger logger = LoggerFactory.getLogger(CrptApi.class);
    private final Semaphore semaphore;
    private final Duration requestInterval;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.semaphore = new Semaphore(requestLimit);
        this.requestInterval = Duration.ofSeconds(timeUnit.toSeconds(1));
    }

    public void createIntroduceGoodsDocument(IntroduceGoodsDocument document) throws IOException, InterruptedException {
        semaphore.acquire();
        try {
            String requestBody = prepareRequestBody(document);
            logger.info("Request body: {}", requestBody);

            simulateRequest();
        } finally {
            semaphore.release();
        }
    }

    private String prepareRequestBody(IntroduceGoodsDocument document) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(document);
    }

    // Inner class representing the document structure
    @Data
    public static class IntroduceGoodsDocument {
        private String participantInn;
        private String docId;
        private String docStatus;
        private String docType;
        private boolean importRequest;
        private String ownerInn;
        private String producerInn;
        private String productionDate;
        private String productionType;

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public void setProductionType(String productionType) {
            this.productionType = productionType;
        }
    }

    private void simulateRequest() {
        logger.info("Simulating request sent successfully!");
    }

    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 10);

        CrptApi.IntroduceGoodsDocument document = new CrptApi.IntroduceGoodsDocument();
        document.setParticipantInn("1234567890");
        document.setDocId("doc123");
        document.setDocStatus("pending");
        document.setDocType("LP_INTRODUCE_GOODS");
        document.setImportRequest(true);
        document.setOwnerInn("0987654321");
        document.setProducerInn("5432109876");
        document.setProductionDate("2023-01-01");
        document.setProductionType("type123");

        try {
            crptApi.createIntroduceGoodsDocument(document);
        } catch (IOException | InterruptedException e) {
            logger.error("Error sending request: {}", e.getMessage());
        }
    }
}
