package com.example.demo.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

//실행 오류로 주석처리 했습니다!
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
public class CommentRequest {
    public record CommentSaveRequest (@NotBlank @Size(min=10) String body){

    }
}
