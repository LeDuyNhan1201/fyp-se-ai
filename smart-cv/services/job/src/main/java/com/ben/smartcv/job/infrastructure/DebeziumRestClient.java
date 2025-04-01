package com.ben.smartcv.job.infrastructure;

import com.ben.smartcv.job.infrastructure.debezium.ConnectionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "debezium-client", url = "http://localhost:8083")
public interface DebeziumRestClient {

    @GetMapping(value = "/connectors", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> getConnections();

    @PostMapping(value = "/connectors", produces = MediaType.APPLICATION_JSON_VALUE)
    void createConnection(@RequestBody ConnectionRequest request);

    @DeleteMapping(value = "/connectors/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteConnection(@PathVariable String name);

}