package com.ben.smartcv.orchestration.saga;

@Saga
@Component
public class ApplyCvSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "cvId")
    public void on(CvAppliedEvent event) {
        commandGateway.send(new ParseCvCommand(event.getCvId()));
    }

    @SagaEventHandler(associationProperty = "cvId")
    public void on(CvNotAppliedEvent event) {
        end();
    }

    @SagaEventHandler(associationProperty = "cvId")
    public void on(CvParsedEvent event) {
        end();
    }

    @SagaEventHandler(associationProperty = "cvId")
    public void on(CvNotParsedEvent event) {
        end();
    }

    @KafkaListener(topics = "cvEvents", groupId = "orchestration-service")
    public void consumeEvent(String eventJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode eventNode = objectMapper.readTree(eventJson);
        String eventType = eventNode.get("type").asText();
        String cvId = eventNode.get("cvId").asText();
        String reason = eventNode.has("reason") ? eventNode.get("reason").asText() : null;

        switch (eventType) {
            case "CvAppliedEvent":
                on(new CvAppliedEvent(cvId));
                break;
            case "CvNotAppliedEvent":
                on(new CvNotAppliedEvent(cvId, reason));
                break;
            case "CvParsedEvent":
                on(new CvParsedEvent(cvId));
                break;
            case "CvNotParsedEvent":
                on(new CvNotParsedEvent(cvId, reason));
                break;
        }
    }
}
