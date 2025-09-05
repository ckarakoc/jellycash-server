package nl.ckarakoc.jellycash.service.impl;

import java.util.List;
import nl.ckarakoc.jellycash.dto.api.v1.pot.CreatePotRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.pot.PartialUpdatePotRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.pot.UpdatePotRequestDto;
import nl.ckarakoc.jellycash.exception.UpdateEntityNotFoundException;
import nl.ckarakoc.jellycash.model.Pot;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.PotRepository;
import nl.ckarakoc.jellycash.service.PotService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PotServiceImpl implements PotService {

  private final ModelMapper modelMapper;
  private final ModelMapper skipNullMapper;
  private final PotRepository potRepository;

  public PotServiceImpl(ModelMapper modelMapper, @Qualifier("skipNullMapper") ModelMapper skipNullMapper, PotRepository potRepository) {
    this.modelMapper = modelMapper;
    this.skipNullMapper = skipNullMapper;
    this.potRepository = potRepository;
  }

  @Override
  public List<Pot> getAllPots(User user) {
    return potRepository.findAllByUser(user);
  }

  @Override
  public Pot getPot(Long id, User user) {
    Pot pot = potRepository.findByPotIdAndUser(id, user)
        .orElseThrow(() -> new UpdateEntityNotFoundException("Pot with id " + id + " not found"));

    return pot;
  }

  @Transactional
  @Override
  public Pot createPot(CreatePotRequestDto dto, User user) {
    Pot created = modelMapper.map(dto, Pot.class);
    created.setUser(user);

    return potRepository.save(created);
  }

  @Transactional
  @Modifying
  @Override
  public Pot updatePot(Long id, UpdatePotRequestDto dto, User user) {
    Pot updated = getPot(id, user);
    modelMapper.map(dto, updated);

    return potRepository.save(updated);
  }

  @Transactional
  @Modifying
  @Override
  public Pot partialUpdatePot(Long id, PartialUpdatePotRequestDto dto, User user) {
    Pot updated = getPot(id, user);
    skipNullMapper.map(dto, updated);
    return potRepository.save(updated);
  }

  @Transactional
  @Override
  public void deletePot(Long id, User user) {
    // no need to verify existence really... 1 less db call
    potRepository.deleteByPotIdAndUser(id, user);
  }
}
