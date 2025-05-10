package com.example.polling.service;

import com.example.polling.dto.VotedPollDto;
import com.example.polling.exception.AlreadyExistsException;
import com.example.polling.exception.ResourceNotFoundException;
import com.example.polling.model.Poll;
import com.example.polling.model.PollField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Сервис для работы с опросами.
 * <p>
 * Предоставляет методы для получения, создания, голосования и удаления опросов.
 * Данные хранятся во временной памяти с использованием потокобезопасного списка.
 * </p>
 *
 * @author
 */
@Service
public class PollService {

    // Потокобезопасное хранилище опросов
    private final List<Poll> pollsStorage = Collections.synchronizedList(new ArrayList<>());

    public PollService() {
        // Инициализация тестовыми данными

        // Опрос 1
        List<PollField> fields1 = new ArrayList<>();
        fields1.add(new PollField("Да", new ArrayList<>(List.of("Кошка с третьего этажа"))));
        fields1.add(new PollField("Нет", new ArrayList<>(List.of("Павел Дуров"))));
        fields1.add(new PollField("Не знаю", new ArrayList<>()));
        pollsStorage.add(new Poll(0, "Тайлер Дёрден", "Любите котов?",
                "Выберите один из вариантов и говорите честно! ведь это очень важно...", fields1));

        // Опрос 2
        List<PollField> fields2 = new ArrayList<>();
        fields2.add(new PollField("Красный", new ArrayList<>(List.of("Павел Дуров"))));
        fields2.add(new PollField("Синий", new ArrayList<>()));
        fields2.add(new PollField("Зеленый", new ArrayList<>(List.of("Кошка с третьего этажа"))));
        fields2.add(new PollField("Другой", new ArrayList<>()));
        pollsStorage.add(new Poll(1, "Кошка с третьего этажа", "Какой ваш любимый цвет?",
                "Ну тип база просто интересно", fields2));

        // Опрос 3
        List<PollField> fields3 = new ArrayList<>();
        fields3.add(new PollField("4", new ArrayList<>()));
        fields3.add(new PollField("5", new ArrayList<>(List.of("Кошка с третьего этажа", "Павел Дуров"))));
        fields3.add(new PollField("10", new ArrayList<>()));
        fields3.add(new PollField("-1", new ArrayList<>()));
        fields3.add(new PollField("42", new ArrayList<>()));
        pollsStorage.add(new Poll(2, "Павел Дуров", "2 + 2 = ?",
                "А вы, друзья мои, сможете решить этот нереальный пример?", fields3));
    }
    /**
     * Возвращает список всех опросов.
     *
     * @return список опросов
     */
    public List<Poll> getAllPolls() {
        return pollsStorage;
    }
    /**
     * Ищет опрос по его идентификатору.
     *
     * @param pollId идентификатор опроса
     * @return найденный опрос
     * @throws ResourceNotFoundException если опрос не найден
     */
    public Poll getPollById(int pollId) {
        return pollsStorage.stream()
                .filter(p -> p.getId() == pollId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));
    }
    /**
     * Возвращает список всех авторов.
     *
     * @return список авторов
     */
    public List<Poll> getPollsByAuthor(String author) {
        List<Poll> result = new ArrayList<>();
        for (Poll poll : pollsStorage) {
            if (poll.getAuthor().equals(author)) {
                result.add(poll);
            }
        }
        return result;
    }
    /**
     * Возвращает список опросов, в которых указанный пользователь проголосовал.
     *
     * @param username имя пользователя
     * @return список объектов {@code VotedPollDto} с информацией об опросах и выбранном варианте
     */
    public List<VotedPollDto> getVotedPolls(String username) {
        List<VotedPollDto> result = new ArrayList<>();
        for (Poll poll : pollsStorage) {
            if (poll.getFields() != null) {
                for (PollField field : poll.getFields()) {
                    if (field.getVotesListDb().contains(username)) {
                        result.add(new VotedPollDto(poll.getId(), poll.getTitle(), field.getTitle()));
                        // Предполагается, что голос учитывается один раз
                        break;
                    }
                }
            }
        }
        return result;
    }
    /**
     * Удаляет опрос с указанным идентификатором.
     *
     * @param pollId идентификатор опроса для удаления
     * @throws ResourceNotFoundException если опрос с данным идентификатором не найден
     */
    public void deletePoll(int pollId) {
        boolean removed = pollsStorage.removeIf(p -> p.getId() == pollId);
        if (!removed) {
            throw new ResourceNotFoundException("Опрос не найден");
        }
    }
    /**
     * Добавляет новый опрос.
     *
     * @param poll объект опроса, который требуется добавить
     * @return добавленный объект опроса
     * @throws AlreadyExistsException если существует опрос с тем же названием и автором
     */
    public Poll addPoll(Poll poll) {
        // Проверка на дублирование по заголовку и автору
        boolean exists = pollsStorage.stream().anyMatch(p ->
                p.getTitle().equals(poll.getTitle()) && p.getAuthor().equals(poll.getAuthor()));
        if (exists) {
            throw new AlreadyExistsException("Опрос с таким названием уже существует");
        }
        pollsStorage.add(poll);
        return poll;
    }
    /**
     * Регистрирует голос пользователя за указанный вариант опроса.
     * Пользователь может проголосовать только один раз в каждом опросе.
     *
     * @param pollId      идентификатор опроса
     * @param username    имя пользователя, который голосует
     * @param optionTitle название выбранного варианта
     * @return обновлённый объект опроса
     * @throws ResourceNotFoundException если опрос или выбранный вариант не найдены
     * @throws RuntimeException          если пользователь уже голосовал в данном опросе
     */
    public Poll vote(int pollId, String username, String optionTitle) {
        Poll poll = getPollById(pollId);

        // Проверка: голосовать можно только один раз в опросе
        for (PollField field : poll.getFields()) {
            if (field.getVotesListDb().contains(username)) {
                throw new RuntimeException("Вы уже проголосовали в этом опросе");
            }
        }
        // Поиск варианта ответа
        PollField option = poll.getFields().stream()
                .filter(f -> f.getTitle().equals(optionTitle))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Вариант ответа не найден"));

        option.getVotesListDb().add(username);
        return poll;
    }
    /**
     * Отменяет голос пользователя в опросе.
     *
     * @param pollId   идентификатор опроса
     * @param username имя пользователя, чей голос необходимо отменить
     * @return обновлённый объект опроса
     * @throws RuntimeException если пользователь не голосовал в данном опросе
     */
    public Poll cancelVote(int pollId, String username) {
        Poll poll = getPollById(pollId);

        for (PollField field : poll.getFields()) {
            if (field.getVotesListDb().contains(username)) {
                field.getVotesListDb().remove(username);
                return poll;
            }
        }
        throw new RuntimeException("Вы не голосовали в этом опросе");
    }
}
