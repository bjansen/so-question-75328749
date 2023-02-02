package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class DemoApplicationTests {

	@PersistenceContext
	private EntityManager entityManager;

	DemoApplicationTests() {
	}

	@Test
	void contextLoads() {
		EntityManager em = entityManager;

		OutFile outFile = new OutFile();
		outFile.setId("outfile");
		em.persist(outFile);

		SpecialOutFile specialOutFile = new SpecialOutFile();
		specialOutFile.setId("specialoutfile");
		specialOutFile.setMoreData("more data");
		em.persist(specialOutFile);

		List<File> files = em.createQuery("from File f order by f.id")
			.getResultList();

		assertEquals(2, files.size());
		assertEquals("outfile", files.get(0).getId());
		assertEquals("specialoutfile", files.get(1).getId());
		assertTrue(files.get(1) instanceof SpecialOutFile);

		List<String> ids = em.createNativeQuery("select id from special_file")
			.getResultList();
		assertEquals(1, ids.size(), "Number of entries in special_file");
		assertEquals("specialoutfile", ids.get(0));

		ids = em.createNativeQuery("select id from file")
			.getResultList();
		assertEquals(1, ids.size(), "Number of entries in file");
		assertEquals("outfile", ids.get(0));
	}

}
