package com.org.util;

import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.impl.BasicTilesContainer;

public class CustomTilesContainer extends BasicTilesContainer {
	
	/**
     * Returns the request context factory.
     *
     * @return The request context factory.
     * @since 2.1.1
     */
    public TilesRequestContextFactory getRequestContextFactory() {
        return super.getRequestContextFactory();
    }

}
