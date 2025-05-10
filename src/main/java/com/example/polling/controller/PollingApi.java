package com.example.polling.controller;

import com.example.polling.dto.CancelVoteRequest;
import com.example.polling.dto.UserLoginDto;
import com.example.polling.dto.UserRegisterDto;
import com.example.polling.dto.VoteRequest;
import com.example.polling.dto.VotedPollDto;
import com.example.polling.model.Poll;
import com.example.polling.model.User;
import com.example.polling.service.PollService;
import com.example.polling.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/polling_api/")
@CrossOrigin(origins = "http://localhost:5173") // Настроено для фронтенда
public class PollingApi {

    @Autowired
    private PollService pollService;

    @Autowired
    private UserService userService;

    // Получение всех опросов
    @GetMapping("/get_all_polls/")
    public List<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }

    // Получение опроса по id
    @GetMapping("/get_poll_by_id/{pollId}/")
    public Poll getPollById(@PathVariable int pollId) {
        return pollService.getPollById(pollId);
    }

    // Получение опросов по автору
    @GetMapping("/get_polls_by_author/{pollsAuthor}/")
    public List<Poll> getPollsByAuthor(@PathVariable String pollsAuthor) {
        return pollService.getPollsByAuthor(pollsAuthor);
    }

    // Получение опросов, в которых пользователь голосовал
    @GetMapping("/get_voted_polls/{username}/")
    public List<VotedPollDto> getVotedPolls(@PathVariable String username) {
        return pollService.getVotedPolls(username);
    }

    // Удаление опроса
    @DeleteMapping("/delete_poll/{pollId}/")
    public Map<String, String> deletePoll(@PathVariable int pollId) {
        pollService.deletePoll(pollId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Опрос успешно удален");
        return response;
    }

    // Добавление нового опроса
    @PostMapping("/add_poll/")
    public Map<String, Object> addPoll(@RequestBody @Valid Poll poll) {
        // Предполагается, что id приходит с фронта (например, unixtime)
        Poll createdPoll = pollService.addPoll(poll);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Опрос успешно добавлен");
        response.put("poll", createdPoll);
        return response;
    }

    // Голосование
    @PostMapping("/vote/")
    public Map<String, Object> vote(@RequestBody @Valid VoteRequest voteRequest) {
        Poll updatedPoll = pollService.vote(voteRequest.getPollId(), voteRequest.getUsername(), voteRequest.getOptionTitle());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ваш голос учтен");
        response.put("poll", updatedPoll);
        return response;
    }

    // Отмена голоса
    @PostMapping("/cancel_vote/")
    public Map<String, Object> cancelVote(@RequestBody @Valid CancelVoteRequest cancelVoteRequest) {
        Poll updatedPoll = pollService.cancelVote(cancelVoteRequest.getPollId(), cancelVoteRequest.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ваш голос отменен");
        response.put("poll", updatedPoll);
        return response;
    }

    // Регистрация пользователя
    @PostMapping("/register/")
    public Map<String, String> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        User user = new User(userRegisterDto.getUsername(), userRegisterDto.getPassword(), null);
        userService.register(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Пользователь успешно зарегистрирован");
        return response;
    }

    // Вход пользователя
    @PostMapping("/login/")
    public Map<String, String> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        User user = userService.login(userLoginDto.getUsername(), userLoginDto.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("registration_date", user.getRegistrationDate());
        return response;
    }

    // Получение полной информации о пользователях (админская ручка)
    @GetMapping("/get_users_db/")
    public Map<String, List<String>> getUsersDb() {
        Map<String, User> users = userService.getUsersDb();
        // Преобразуем модель User в Map<String, List<String>> (password, registrationDate)
        Map<String, List<String>> response = new HashMap<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();
            response.put(entry.getKey(), List.of(user.getPassword(), user.getRegistrationDate()));
        }
        return response;
    }
}
