package org.hibernate.orm.test.component;

import org.hibernate.MappingException;
import org.hibernate.annotations.Struct;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;

import org.hibernate.testing.orm.junit.DialectFeatureChecks;
import org.hibernate.testing.orm.junit.JiraKey;
import org.hibernate.testing.orm.junit.RequiresDialectFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@RequiresDialectFeature(feature = DialectFeatureChecks.SupportsStructAggregate.class)
public class StructComponentCollectionError3Test {

	@Test
	@JiraKey( "HHH-15862" )
	public void testError() {
		final StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.applySetting( AvailableSettings.HBM2DDL_AUTO, "create-drop" ).build();
		try {
			new MetadataSources( ssr )
					.addAnnotatedClass( Book.class )
					.getMetadataBuilder()
					.build()
					.buildSessionFactory();
			Assertions.fail( "Expected a failure" );
		}
		catch (MappingException ex) {
			Assertions.assertTrue( ex.getMessage().contains( "tags" ) );
		}
		finally {
			StandardServiceRegistryBuilder.destroy( ssr );
		}
	}


	@Entity(name = "Book")
	public static class Book {

		@Id
		@GeneratedValue
		private Long id;
		private String title;
		private Person author;
	}

	@Embeddable
	@Struct(name = "person_type")
	public static class Person {
		private String name;
		private String[] tags;
	}

}
