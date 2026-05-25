package com.capstone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DevEnvironmentGuardTests {

	@TempDir
	Path tempDir;

	@Test
	void findsStampInRepositoryRoot() throws IOException {
		Path repoRoot = tempDir.resolve("repo");
		Files.createDirectories(repoRoot);
		Path stamp = Files.createFile(repoRoot.resolve(".dev-env-stamp"));

		assertEquals(stamp, DevEnvironmentGuard.locateStampFile(repoRoot).orElseThrow());
	}

	@Test
	void findsStampInAppDirectory() throws IOException {
		Path repoRoot = tempDir.resolve("repo");
		Path appDirectory = repoRoot.resolve("app");
		Files.createDirectories(appDirectory);
		Path stamp = Files.createFile(appDirectory.resolve(".dev-env-stamp"));

		assertEquals(stamp, DevEnvironmentGuard.locateStampFile(appDirectory).orElseThrow());
	}

	@Test
	void rejectsStartupWhenSetupHasNotBeenRun() {
		Path repoRoot = tempDir.resolve("repo");

		IllegalStateException exception = assertThrows(
			IllegalStateException.class,
			() -> DevEnvironmentGuard.requireSetupComplete(repoRoot)
		);

		assertTrue(exception.getMessage().contains("./dev_scripts/setup_dev.sh"));
	}
}
