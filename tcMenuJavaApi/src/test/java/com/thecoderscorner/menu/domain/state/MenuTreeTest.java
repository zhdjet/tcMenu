/*
 * Copyright (c) 2018 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 */

package com.thecoderscorner.menu.domain.state;

import com.google.common.collect.ImmutableList;
import com.thecoderscorner.menu.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class MenuTreeTest {

    private MenuTree menuTree;
    private EnumMenuItem item1 = DomainFixtures.anEnumItem("Item1", 1);
    private EnumMenuItem item2 = DomainFixtures.anEnumItem("Item2", 2);
    private AnalogMenuItem item3 = DomainFixtures.anAnalogItem("Item3", 3);
    private SubMenuItem subMenu = DomainFixtures.aSubMenu("Sub1", 4);


    @Before
    public void setUp() throws Exception {
        menuTree = new MenuTree();
    }

    @Test
    public void testAddingItemsThenRemoving() {
        menuTree.addMenuItem(MenuTree.ROOT, item1);
        menuTree.addMenuItem(null, item2); // null acts the same as ROOT

        assertEquals(2, menuTree.getMenuItems(MenuTree.ROOT).size());

        menuTree.removeMenuItem(MenuTree.ROOT, item1);

        ImmutableList<MenuItem> menuItems = menuTree.getMenuItems(MenuTree.ROOT);
        assertThat(menuItems, is(Collections.singletonList(item2)));
    }

    @Test
    public void testThatRemovingMenuItemRemovesState() {
        menuTree.addMenuItem(MenuTree.ROOT, item1);
        menuTree.changeItem(item1, item1.newMenuState(1, true, false));

        assertNotNull(menuTree.getMenuState(item1));

        menuTree.removeMenuItem(MenuTree.ROOT, item1);
        assertNull(menuTree.getMenuState(item1));
    }

    @Test
    public void testSubMenuKeysAreCreatedAndRemoved() {
        menuTree.addMenuItem(MenuTree.ROOT, subMenu);
        assertTrue(menuTree.getAllSubMenus().contains(subMenu));
        assertTrue(menuTree.getAllSubMenus().contains(MenuTree.ROOT));

        menuTree.addMenuItem(subMenu, item1);
        menuTree.addMenuItem(subMenu, item2);
        assertThat(menuTree.getMenuItems(subMenu), is(Arrays.asList(item1, item2)));

        menuTree.removeMenuItem(subMenu, item1);
        assertThat(menuTree.getMenuItems(subMenu), is(Collections.singletonList(item2)));

        menuTree.removeMenuItem(MenuTree.ROOT, subMenu);
        assertNull(menuTree.getMenuItems(subMenu));
    }

    @Test
    public void testManipulatingState() {
        menuTree.addMenuItem(MenuTree.ROOT, subMenu);
        menuTree.addMenuItem(MenuTree.ROOT, item1);
        menuTree.addMenuItem(subMenu, item3);

        menuTree.changeItem(item1, item1.newMenuState(1, true, false));
        MenuState state = menuTree.getMenuState(item1);
        assertTrue(state instanceof IntegerMenuState);
        assertEquals(1, state.getValue());
        assertTrue(state.isChanged());
        assertFalse(state.isActive());

        menuTree.changeItem(item3, item3.newMenuState(1, false, true));
        MenuState stateAnalog = menuTree.getMenuState(item3);
        assertTrue(stateAnalog instanceof IntegerMenuState);
        assertEquals(1, stateAnalog.getValue());
        assertFalse(stateAnalog.isChanged());
        assertTrue(stateAnalog.isActive());

        menuTree.changeItem(subMenu, subMenu.newMenuState(true, false, true));
        MenuState stateSubMenu = menuTree.getMenuState(subMenu);
        assertTrue(stateSubMenu instanceof BooleanMenuState);
        assertEquals(true, stateSubMenu.getValue());
        assertFalse(stateSubMenu.isChanged());
        assertTrue(stateSubMenu.isActive());

    }

    @Test
    public void testReplaceById() {
        menuTree.addMenuItem(MenuTree.ROOT, item3);
        menuTree.addMenuItem(MenuTree.ROOT, subMenu);
        menuTree.addMenuItem(subMenu, item1);
        EnumMenuItem item1Change = DomainFixtures.anEnumItem("Changed item1", 1);
        menuTree.replaceMenuById(item1Change);

        assertThat(menuTree.getMenuItems(subMenu), is(Collections.singletonList(item1Change)));
    }

    @Test
    public void testRemoveWhereParentNotSpecified() {
        menuTree.addMenuItem(MenuTree.ROOT, item3);
        menuTree.addMenuItem(MenuTree.ROOT, subMenu);
        menuTree.addMenuItem(subMenu, item1);
        menuTree.addMenuItem(subMenu, item2);

        assertThat(menuTree.getMenuItems(subMenu), is(Arrays.asList(item1, item2)));
        menuTree.removeMenuItem(item2);
        assertThat(menuTree.getMenuItems(subMenu), is(Collections.singletonList(item1)));
    }

    @Test
    public void testMovingItemsAround() {
        menuTree.addMenuItem(MenuTree.ROOT, item3);
        menuTree.addMenuItem(MenuTree.ROOT, item1);
        menuTree.addMenuItem(MenuTree.ROOT, item2);

        menuTree.moveItem(MenuTree.ROOT, item2, MenuTree.MoveType.MOVE_UP);
        assertThat(menuTree.getMenuItems(MenuTree.ROOT), is(Arrays.asList(item3, item2, item1)));

        menuTree.moveItem(MenuTree.ROOT, item3, MenuTree.MoveType.MOVE_DOWN);
        assertThat(menuTree.getMenuItems(MenuTree.ROOT), is(Arrays.asList(item2, item3, item1)));

        menuTree.moveItem(MenuTree.ROOT, item1, MenuTree.MoveType.MOVE_DOWN);
        assertThat(menuTree.getMenuItems(MenuTree.ROOT), is(Arrays.asList(item2, item3, item1)));

        menuTree.moveItem(MenuTree.ROOT, item2, MenuTree.MoveType.MOVE_UP);
        assertThat(menuTree.getMenuItems(MenuTree.ROOT), is(Arrays.asList(item2, item3, item1)));

        menuTree.moveItem(MenuTree.ROOT, item1, MenuTree.MoveType.MOVE_UP);
        assertThat(menuTree.getMenuItems(MenuTree.ROOT), is(Arrays.asList(item2, item1, item3)));
    }
}