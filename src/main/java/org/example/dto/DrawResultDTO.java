package org.example.dto;

import java.util.List;

public class DrawResultDTO {

    public static class TicketResult {
        public Long id;
        public List<Integer> numbers;
        public boolean isWinner;

        public TicketResult(Long id, List<Integer> numbers, boolean isWinner) {
            this.id = id;
            this.numbers = numbers;
            this.isWinner = isWinner;
        }
    }

    public Long drawId;
    public List<Integer> winningNumbers;
    public List<TicketResult> tickets;

    public DrawResultDTO(Long drawId, List<Integer> winningNumbers, List<TicketResult> tickets) {
        this.drawId = drawId;
        this.winningNumbers = winningNumbers;
        this.tickets = tickets;
    }
}
