package nl.ckarakoc.jellycash.service.impl;

import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.enums.AppRole;
import nl.ckarakoc.jellycash.repository.RoleRepository;
import nl.ckarakoc.jellycash.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	//todo: private final Map<AppRole, Role> roleCache = new HashMap<>();


	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role getRole(AppRole appRole) {
		return roleRepository.findByRole(appRole).orElseThrow(() -> new RuntimeException("Role not found"));
	}
}
