package com.jhsfully.inventoryManagement.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{

    private final LogRepository logRepository;

    @Override
    public void addLog(LogDto.AddRequest request) {
        LogEntity log = LogEntity.builder()
                .username(request.getUsername())
                .signature(request.getSignature())
                .requestUrl(request.getRequestUrl())
                .method(request.getMethod())
                .elapsed(request.getElapsed())
                .at(request.getAt())
                .is_success(request.is_success())
                .build();

        logRepository.save(log);
    }

    @Override
    public void deleteLogs(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        logRepository.deleteByAtBetween(startDateTime, endDateTime);
    }

    @Override
    public List<LogDto.Response> getLogs(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return logRepository.findByAtBetween(startDateTime, endDateTime)
                .stream()
                .map(LogDto.Response::of)
                .collect(Collectors.toList());
    }
}
