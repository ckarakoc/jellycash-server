package nl.ckarakoc.jellycash.service;

import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.enums.AppRole;

public interface RoleService {
	Role getRole(AppRole appRole);
}
