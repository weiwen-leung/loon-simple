package org.loon.framework.game.simple.extend.db;

public interface Serializer {
	
	public byte[] getBytes(final Object o);

	public Object getObject(final byte[] b);
	
	public int getType();
	
}
