package com.lab2.server.services

import com.lab2.server.data.Status
import com.lab2.server.dto.TicketCreateBodyDTO
import com.lab2.server.dto.TicketDTO

interface TicketService {
    fun getAll(): List<TicketDTO>
    fun getTicketByID(ticketId: Long): TicketDTO?
    fun insertTicket(ticket: TicketCreateBodyDTO)
    fun setTicketStatus(ticketId: Long, inputStatus: Status, bodyExpert: Long?)
}