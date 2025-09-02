package nl.ckarakoc.jellycash.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nl.ckarakoc.jellycash.dto.api.v1.pot.CreatePotRequestDto;
import nl.ckarakoc.jellycash.model.Pot;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.PotRepository;
import nl.ckarakoc.jellycash.service.PotService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PotServiceImpl implements PotService {

  private final ModelMapper modelMapper;
  private final PotRepository potRepository;

  @Override
  public Optional<Pot> createPot(CreatePotRequestDto dto, User user) {
    Pot created = modelMapper.map(dto, Pot.class);
    created.setUser(user);

    Pot saved = potRepository.save(created);

    return Optional.of(saved);
  }
}
