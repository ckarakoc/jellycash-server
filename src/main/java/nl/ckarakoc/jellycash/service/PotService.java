package nl.ckarakoc.jellycash.service;

import java.util.List;
import nl.ckarakoc.jellycash.dto.api.v1.pot.CreatePotRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.pot.PartialUpdatePotRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.pot.UpdatePotRequestDto;
import nl.ckarakoc.jellycash.model.Pot;
import nl.ckarakoc.jellycash.model.User;

public interface PotService {

  Pot createPot(CreatePotRequestDto dto, User user);

  List<Pot> getAllPots(User user);

  Pot getPot(Long id, User user);

  Pot updatePot(Long id, UpdatePotRequestDto dto, User user);

  Pot partialUpdatePot(Long id, PartialUpdatePotRequestDto dto, User user);

  void deletePot(Long id, User user);
}
