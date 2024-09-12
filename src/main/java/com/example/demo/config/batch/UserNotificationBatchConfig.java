package com.example.demo.config.batch;

import com.example.demo.domain.member.email.application.memberemailservice.MemberEmailService;
import com.example.demo.domain.member.member.domain.Member;
import com.example.demo.domain.member.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@EnableScheduling
public class UserNotificationBatchConfig {
    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final MemberRepository memberRepository;

    private final MemberEmailService memberEmailService;

    private final JobLauncher jobLauncher;

    @Bean
    public Job memberNotificationJob(){
        return new JobBuilder("memberNotificationJob", jobRepository)
                .start(memberNotigicationStep())
                .build();
    }

    @Bean
    public Step memberNotigicationStep(){
        return new StepBuilder("memberNotificationStep", jobRepository)
                .tasklet(memberNotificationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet memberNotificationTasklet(){
        return (contribution, chunkContext) -> {
            // 현재 날짜에서 5개월 이상 미접속한 회원들을 List 로 정리
            List<Member> inactiveMembers = memberRepository.findByLastLoginDateBefore(LocalDate.now().minusDays(1));
            for(Member member:inactiveMembers){
                // 장기 미접속한 회원들에게 메일 전송
                memberEmailService.sendNotification(member.getEmail());
            }
            return RepeatStatus.FINISHED;
        };
    }

    // 스케줄러 생성, 매일 오후 5시에 실행
    @Scheduled(cron = "0 0 17 * * ?")
    public void runJob() throws Exception {
        // JobLauncher 로 spring batch 실행
        JobExecution execution = jobLauncher.run(memberNotificationJob(), new JobParameters());
        log.info("Job Status : " + execution.getStatus());

    }

}
