package nl.ckarakoc.jellycash.service;

import jakarta.validation.Valid;
import java.util.Optional;
import nl.ckarakoc.jellycash.dto.api.v1.pot.CreatePotRequestDto;
import nl.ckarakoc.jellycash.model.Pot;
import nl.ckarakoc.jellycash.model.User;

public interface PotService {
  Optional<Pot> createPot(@Valid CreatePotRequestDto dto, User user);
}
