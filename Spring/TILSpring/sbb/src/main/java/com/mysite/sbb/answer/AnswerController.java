package com.mysite.sbb.answer;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import java.security.Principal;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, 
    /* @RequestParam String content */ @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal){
        // requestParam String Content : 템플릿에서 답변으로 입력 내용(content)을 얻음
        // 템플릿의 답변 내용에 해당하는 textarea name 속성명이 content
        // principal.getName() :: 로그인한 사용자의 사용자 ID를 호출할 수 있음
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        // principal 객체 -> 사용자명 얻은 후 -> SiteUser객체 얻어서 답변 등록

        if(bindingResult.hasErrors()){
            model.addAttribute("question", question);
            return "question_detail";
            // 요청에 실패한 경우 다시 question_detail 템플릿을 렌더링함
            // 이 때 question_detail 템플릿은 Question 객체가 필요하므로 model 객체에 Question 객체 저장
        }
        this.answerService.create(question, answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
