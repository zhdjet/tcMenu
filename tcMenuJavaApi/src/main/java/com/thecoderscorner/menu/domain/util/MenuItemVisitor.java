/*
 * Copyright (c) 2018 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 */

package com.thecoderscorner.menu.domain.util;

import com.thecoderscorner.menu.domain.AnalogMenuItem;
import com.thecoderscorner.menu.domain.BooleanMenuItem;
import com.thecoderscorner.menu.domain.EnumMenuItem;
import com.thecoderscorner.menu.domain.SubMenuItem;

public interface MenuItemVisitor {
    void visit(AnalogMenuItem item);
    void visit(BooleanMenuItem item);
    void visit(EnumMenuItem item);
    void visit(SubMenuItem item);
}