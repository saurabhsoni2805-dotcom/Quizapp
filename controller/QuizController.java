package com.example.Quizapp.controller;

import com.example.Quizapp.model.Question;
import com.example.Quizapp.model.User;
import com.example.Quizapp.service.QuestionsService;
import com.example.Quizapp.service.QuizUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class QuizController {

    private final QuestionsService questionsService;
    private final QuizUserDetailsService userDetailsService;

    public QuizController(QuestionsService questionsService, QuizUserDetailsService userDetailsService) {
        this.questionsService = questionsService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userDetailsService.registerUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
        return "redirect:/login";
    }

    @GetMapping("/quizList")
    public String showQuizList(Model model) {
        model.addAttribute("quizzes", questionsService.loadQuizzes());
        return "quizList";
    }

    @GetMapping("/addQuiz")
    public String showAddQuiz(Model model) {
        Question q = new Question();
        q.setOptions(new ArrayList<>(Arrays.asList("", "", "", "")));
        model.addAttribute("question", q);
        return "addQuiz";
    }

    @PostMapping("/addQuiz")
    public String addQuiz(@ModelAttribute Question question) {
        questionsService.addQuiz(question);
        return "redirect:/quizList";
    }

    @GetMapping("/editQuiz/{id}")
    public String showEditQuiz(@PathVariable int id, Model model) {
        model.addAttribute("question", questionsService.getQuestionById(id));
        return "editQuiz";
    }

    @PutMapping("/editQuiz")
    public String editQuiz(@ModelAttribute Question question) {
        questionsService.editQuiz(question);
        return "redirect:/quizList";
    }

    @DeleteMapping("/deleteQuiz/{id}")
    public String deleteQuiz(@PathVariable int id) {
        questionsService.deleteQuiz(id);
        return "redirect:/quizList";
    }

    @GetMapping("/quiz")
    public String showQuiz(Model model) {
        model.addAttribute("quizzes", questionsService.loadQuizzes());
        return "quiz";
    }

    @PostMapping("/submitQuiz")
    public String submitQuiz(@RequestParam Map<String, String> answers, Model model) {
        List<Question> quizzes = questionsService.loadQuizzes();
        int score = 0;
        for (Question q : quizzes) {
            String submitted = answers.get("question_" + q.getId());
            if (submitted != null && submitted.trim().equalsIgnoreCase(q.getCorrectAnswer().trim())) {
                score++;
            }
        }
        model.addAttribute("score", score);
        model.addAttribute("total", quizzes.size());
        return "result";
    }
}
