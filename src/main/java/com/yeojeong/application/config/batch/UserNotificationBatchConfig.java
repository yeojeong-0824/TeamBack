package com.yeojeong.application.config.batch;

import com.yeojeong.application.config.email.EmailManager;
import com.yeojeong.application.config.email.EmailSender;
import com.yeojeong.application.domain.member.domain.Member;
import com.yeojeong.application.domain.member.domain.MemberRepository;
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
    private final JobLauncher jobLauncher;

    private final EmailManager emailManager;

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
            List<Member> inactiveMembers = memberRepository.findByLastLoginDateBefore(LocalDate.now().minusDays(1));
            for(Member member:inactiveMembers){
                emailManager.notification(member.getEmail());
            }
            return RepeatStatus.FINISHED;
        };
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void runJob() throws Exception {
        JobExecution execution = jobLauncher.run(memberNotificationJob(), new JobParameters());
        log.info("Job Status : " + execution.getStatus());
    }

}
