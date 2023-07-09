package com.jhsfully.inventoryManagement.log;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Scheduled(cron = "0 0 * * * *") //1시간 마다 동작
    public void deleteLogs() {

        //현재 날짜에서, 2주 정도뒤에 날짜를 가져와서 해당 날짜 이전인 로그 전부 지우기
        LocalDateTime prevDateTime = LocalDate.now().minusWeeks(2).atStartOfDay();
        List<LogEntity> logs = logRepository.findByAtBefore(prevDateTime);
        logRepository.deleteAll(logs);

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
