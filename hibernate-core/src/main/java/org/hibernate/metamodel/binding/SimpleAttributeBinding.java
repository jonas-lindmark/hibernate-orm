/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.metamodel.binding;

import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.state.domain.SimpleAttributeDomainState;
import org.hibernate.metamodel.state.relational.ColumnRelationalState;
import org.hibernate.metamodel.state.relational.DerivedValueRelationalState;
import org.hibernate.metamodel.state.relational.TupleRelationalState;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class SimpleAttributeBinding extends SingularAttributeBinding {
	private PropertyGeneration generation;

	SimpleAttributeBinding(EntityBinding entityBinding, boolean forceNonNullable, boolean forceUnique) {
		super( entityBinding, forceNonNullable, forceUnique );
	}

	public final void initialize(SimpleAttributeDomainState state) {
		super.initialize( state );
		generation = state.getPropertyGeneration();
	}

	public void initializeColumnValue(ColumnRelationalState state) {
		super.initializeValue( state );
	}

	public void initializeDerivedValue(DerivedValueRelationalState state) {
		super.initializeValue( state );
	}

	public void initializeTupleValue(TupleRelationalState state) {
		super.initializeValue( state );
	}

	private boolean isUnique(ColumnRelationalState state) {
		return isPrimaryKey() || state.isUnique();
	}

	@Override
	public boolean isSimpleValue() {
		return true;
	}

	private boolean isPrimaryKey() {
		return this == getEntityBinding().getEntityIdentifier().getValueBinding();
	}

	public PropertyGeneration getGeneration() {
		return generation;
	}

}
