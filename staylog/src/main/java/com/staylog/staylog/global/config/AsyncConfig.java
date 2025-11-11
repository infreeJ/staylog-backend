package com.staylog.staylog.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 스레드 풀 관리를 위한 TaskExecutor 설정 클래스
 * @author 이준혁
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * SimpleAsyncTaskExecutor를 사용하지 않도록 커스텀 TaskExecutor 정의
     * TODO: 로컬 개발환경에서는 현재 설정이 무리없이 동작하지만 t3.small에서 다시 확인 필요
     * @author 이준혁
     * @return Executor
     */
    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 기본적으로 대기하는 코어 스레드 수
        executor.setCorePoolSize(4);

        // 최대 스레드 수(코어 스레드가 모두 사용되고 있고, 큐도 가득 찼을 때 늘어난다.)
        executor.setMaxPoolSize(8);

        // 큐 용량(코어 스레드가 모두 사용 중일 때 대기시키는 큐)
        executor.setQueueCapacity(100);

        // 모니터링을 위한 스레드 이름 설정
        executor.setThreadNamePrefix("staylog-async-");

        // 서버 종료 시 큐에 남은 작업을 기다릴지 여부
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 서버 종료 시 최대 대기 시간 (넘으면 강제 종료)
        executor.setAwaitTerminationSeconds(5);

        executor.initialize();
        return executor;
    }
}
