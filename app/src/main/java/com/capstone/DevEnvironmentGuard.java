package com.capstone;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

final class DevEnvironmentGuard {

	private static final String STAMP_FILE_NAME = ".dev-env-stamp";
	private static final String SETUP_COMMAND = "./dev_scripts/setup_dev.sh";
	private static final String MISSING_SETUP_MESSAGE =
		"Development setup not completed. Run " + SETUP_COMMAND
			+ " from the repository root before starting the application.";

	private DevEnvironmentGuard() {
	}

	static void requireSetupComplete() {
		requireSetupComplete(Path.of("").toAbsolutePath().normalize());
	}

	static void requireSetupComplete(Path workingDirectory) {
		if (locateStampFile(workingDirectory).isPresent()) {
			return;
		}

		throw new IllegalStateException(MISSING_SETUP_MESSAGE);
	}

	static Optional<Path> locateStampFile(Path workingDirectory) {
		for (Path candidate : stampCandidates(workingDirectory)) {
			if (Files.isRegularFile(candidate)) {
				return Optional.of(candidate);
			}
		}

		return Optional.empty();
	}

	private static Set<Path> stampCandidates(Path workingDirectory) {
		Path normalizedWorkingDirectory = workingDirectory.toAbsolutePath().normalize();
		Set<Path> candidates = new LinkedHashSet<>();

		candidates.add(normalizedWorkingDirectory.resolve(STAMP_FILE_NAME));
		candidates.add(normalizedWorkingDirectory.resolve("app").resolve(STAMP_FILE_NAME));

		Path parent = normalizedWorkingDirectory.getParent();
		if (parent != null) {
			candidates.add(parent.resolve(STAMP_FILE_NAME));
			candidates.add(parent.resolve("app").resolve(STAMP_FILE_NAME));
		}

		return candidates;
	}
}
