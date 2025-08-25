package nl.ckarakoc.jellycash.config;

import nl.ckarakoc.jellycash.model.Role;
import nl.ckarakoc.jellycash.model.enums.AppRole;
import nl.ckarakoc.jellycash.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;

	public DBInitializer(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Initializing database data...");

		System.out.println("Inserting roles...");
		for(AppRole role : AppRole.values()) {
			boolean exist = roleRepository.existsByRole(role);
			if (!exist) {
				Role newRole = new Role();
				newRole.setRole(role);
				roleRepository.save(newRole);
				System.out.println("Inserted role: " + role);
			}
		}
	}
}
