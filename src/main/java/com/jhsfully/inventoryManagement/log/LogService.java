package com.jhsfully.inventoryManagement.log;

import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

public interface LogService {

    void addLog(@RequestBody LogDto.AddRequest request);
    void deleteLogs();
    List<LogDto.Response> getLogs(LocalDate startDate, LocalDate endDate);

}
