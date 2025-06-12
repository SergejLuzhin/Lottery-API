package org.example.service;

import org.example.model.Draw;
import org.example.model.Ticket;
import org.example.dto.DrawResultDTO;
import org.example.dto.DrawResultDTO.TicketResult;
import org.example.repository.DrawRepository;
import org.example.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DrawService {

    private final DrawRepository drawRepository;
    private final TicketRepository ticketRepository;

    public DrawService(DrawRepository drawRepository, TicketRepository ticketRepository) {
        this.drawRepository = drawRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Draw createDraw() {
        Optional<Draw> existingActive = drawRepository.findByStatus(Draw.Status.ACTIVE);
        if (existingActive.isPresent()) {
            throw new IllegalStateException("Активный тираж уже существует.");
        }

        Draw draw = new Draw();
        draw.setStatus(Draw.Status.ACTIVE);
        return drawRepository.save(draw);
    }

    @Transactional
    public Draw closeDraw(Long drawId) {
        Draw draw = drawRepository.findById(drawId)
                .orElseThrow(() -> new NoSuchElementException("Тираж не найден"));

        if (draw.getStatus() != Draw.Status.ACTIVE) {
            throw new IllegalStateException("Тираж уже закрыт");
        }

        Set<Integer> winning = new HashSet<>();
        Random random = new Random();
        while (winning.size() < 5) {
            winning.add(random.nextInt(36) + 1);
        }

        draw.setWinningNumbers(new ArrayList<>(winning));
        draw.setStatus(Draw.Status.CLOSED);
        return drawRepository.save(draw);
    }

    @Transactional(readOnly = true)
    public DrawResultDTO getDrawResults(Long drawId) {
        Draw draw = drawRepository.findById(drawId)
                .orElseThrow(() -> new NoSuchElementException("Тираж не найден"));

        if (draw.getStatus() != Draw.Status.CLOSED) {
            throw new IllegalStateException("Тираж ещё не закрыт");
        }

        List<Integer> winning = draw.getWinningNumbers();
        List<Ticket> allTickets = ticketRepository.findByDraw(draw);

        List<TicketResult> results = allTickets.stream().map(ticket -> {
            boolean isWinner = new HashSet<>(ticket.getNumbers()).equals(new HashSet<>(winning));
            return new TicketResult(ticket.getId(), ticket.getNumbers(), isWinner);
        }).toList();

        return new DrawResultDTO(draw.getId(), winning, results);
    }
}
