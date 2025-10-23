package com.staylog.staylog.test;

import java.sql.Connection;
import javax.sql.DataSource;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OracleDBConnectionTest {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public OracleDBConnectionTest(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    // 서버 시작 완료 후 실행되는 이벤트 리스너
    @EventListener(ApplicationReadyEvent.class)
    public void testConnection() {
        System.out.println("🔍 Oracle DB 연결 테스트 시작...");

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ Oracle DB 연결 성공!");
            System.out.println("드라이버: " + connection.getMetaData().getDriverName());
            System.out.println("URL: " + connection.getMetaData().getURL());
            System.out.println("유저명: " + connection.getMetaData().getUserName());

            // 간단한 쿼리 테스트
            String result = jdbcTemplate.queryForObject("SELECT 'Hello Oracle!' FROM dual", String.class);
            System.out.println("테스트 쿼리 결과: " + result);

        } catch (Exception e) {
            System.out.println("❌ DB 연결 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}