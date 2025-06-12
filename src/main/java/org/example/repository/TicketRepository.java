package org.example.repository;

import org.example.model.Ticket;
import org.example.model.Draw;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByDraw(Draw draw);
}
