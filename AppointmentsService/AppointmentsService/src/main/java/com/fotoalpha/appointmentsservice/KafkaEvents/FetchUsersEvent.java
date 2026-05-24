package com.fotoalpha.appointmentsservice.KafkaEvents;

import com.fotoalpha.appointmentsservice.Entity.User;

import java.util.List;

public record FetchUsersEvent(List<User> users, String correlationId)  {
}
