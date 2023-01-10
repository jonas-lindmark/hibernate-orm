/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.hql;

import java.util.List;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DomainModel(
		annotatedClasses = {
				HqlSubselectParameterTest.Resource.class,
				HqlSubselectParameterTest.Bookmark.class
		}
)
@SessionFactory
@TestForIssue(jiraKey = "HHH-15647")
public class HqlSubselectParameterTest {

	@BeforeAll
	public void setUp(SessionFactoryScope scope) {
		scope.inTransaction(
				session -> {
					Resource resource = new Resource();
					session.persist( resource );
					Bookmark bookmark = new Bookmark();
					session.persist( bookmark );

				}
		);
	}

	@Test
	public void testHqlQuery(SessionFactoryScope scope) {
		scope.inTransaction(
				session -> {
					String query = "select rsrc, (select count(mrk.key) from bookmark as mrk where mrk.key=:identityKey) as myBookmarks from resource as rsrc";
					List<Object[]> objects = session.createQuery( query, Object[].class )
							.setParameter( "identityKey", 100l )
							.getResultList();

					assertThat( objects.size() ).isEqualTo( 1 );
				}
		);
	}

	@Entity(name = "bookmark")
	@Table(name = "o_bookmark")
	public static class Bookmark {

		@Id
		@Column(name = "id", nullable = false, unique = true, updatable = false)
		private Long key;

		private String name;

	}

	@Entity(name = "resource")
	@Table(name = "o_resource")
	public static class Resource {

		@Id
		@Column(name = "id", nullable = false, unique = true, updatable = false)
		private Long key;

		private String name;

	}
}
