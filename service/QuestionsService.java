package com.example.Quizapp.service;

import com.example.Quizapp.model.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestionsService {

    private final Map<Integer, Question> questions = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public QuestionsService() {
        addQuiz(new Question(0, "Which language is used for Spring Boot?",
                new ArrayList<>(Arrays.asList("Java", "Python", "C#", "Ruby")), "Java"));
        addQuiz(new Question(0, "Which annotation marks a class as a Spring controller?",
                new ArrayList<>(Arrays.asList("@Service", "@Controller", "@Repository", "@Bean")), "@Controller"));
    }

    public List<Question> loadQuizzes() {
        return new ArrayList<>(questions.values());
    }

    public void addQuiz(Question question) {
        int id = idCounter.getAndIncrement();
        question.setId(id);
        questions.put(id, question);
    }

    public void editQuiz(Question question) {
        questions.put(question.getId(), question);
    }

    public void deleteQuiz(int id) {
        questions.remove(id);
    }

    public Question getQuestionById(int id) {
        return questions.get(id);
    }
}
