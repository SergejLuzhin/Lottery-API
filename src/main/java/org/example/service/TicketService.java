package org.example.service;

import org.example.model.Draw;
import org.example.model.Ticket;
import org.example.repository.DrawRepository;
import org.example.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final DrawRepository drawRepository;

    public TicketService(TicketRepository ticketRepository, DrawRepository drawRepository) {
        this.ticketRepository = ticketRepository;
        this.drawRepository = drawRepository;
    }

    @Transactional
    public Ticket createTicket(List<Integer> numbers) {
        // Валидация количества
        if (numbers.size() != 5) {
            throw new IllegalArgumentException("Нужно выбрать ровно 5 чисел.");
        }

        // Уникальность
        Set<Integer> unique = new HashSet<>(numbers);
        if (unique.size() != 5) {
            throw new IllegalArgumentException("Числа не должны повторяться.");
        }

        // Диапазон
        for (Integer num : numbers) {
            if (num < 1 || num > 36) {
                throw new IllegalArgumentException("Числа должны быть от 1 до 36.");
            }
        }

        // Поиск активного тиража
        Draw draw = drawRepository.findByStatus(Draw.Status.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Нет активного тиража."));

        // Создание билета
        Ticket ticket = new Ticket();
        ticket.setNumbers(numbers);
        ticket.setDraw(draw);

        return ticketRepository.save(ticket);
    }
}
