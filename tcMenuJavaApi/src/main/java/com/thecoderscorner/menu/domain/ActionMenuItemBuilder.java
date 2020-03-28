/*
 * Copyright (c)  2016-2019 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 *
 */

package com.thecoderscorner.menu.domain;

/**
 * Constructs an ActionMenuItemBuilder using the standard builder pattern. It is possible to either build
 * an item from scratch, or start with an existing item and make changes.
 */
public class ActionMenuItemBuilder extends MenuItemBuilder<ActionMenuItemBuilder> {

    @Override
    public ActionMenuItemBuilder getThis() {
        return this;
    }

    public ActionMenuItemBuilder withExisting(ActionMenuItem item) {
        baseFromExisting(item);
        return getThis();
    }

    public ActionMenuItem menuItem() {
        return new ActionMenuItem(name, id, functionName, eepromAddr, readOnly, localOnly, visible);
    }

    public static ActionMenuItemBuilder anActionMenuItemBuilder() {
        return new ActionMenuItemBuilder();
    }

}
