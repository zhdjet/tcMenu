/*
 * Copyright (c) 2018 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 */

package com.thecoderscorner.menu.domain.state;

import com.google.common.collect.ImmutableList;
import com.thecoderscorner.menu.domain.MenuItem;
import com.thecoderscorner.menu.domain.SubMenuItem;
import com.thecoderscorner.menu.domain.util.MenuItemHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MenuTree {
    public enum MoveType { MOVE_UP, MOVE_DOWN }
    public static final SubMenuItem ROOT = new SubMenuItem("Root", 0, -1);
    private static final int EXPECTED_MAX_VALUES = 256;

    private final Map<Integer, MenuState<?>> menuStates = new ConcurrentHashMap<>(EXPECTED_MAX_VALUES);
    private final Map<MenuItem, ArrayList<MenuItem>> subMenuItems = new HashMap<>(EXPECTED_MAX_VALUES / 8);

    public MenuTree() {
        subMenuItems.put(ROOT, new ArrayList<>());
    }

    public void addMenuItem(SubMenuItem parent, MenuItem item) {
        SubMenuItem subMenu = (parent != null) ? parent : ROOT;

        synchronized (subMenuItems) {
            ArrayList<MenuItem> subMenuChildren = subMenuItems.computeIfAbsent(subMenu, sm -> new ArrayList<>());
            subMenuChildren.add(item);

            if (item.hasChildren()) {
                subMenuItems.put(item, new ArrayList<>());
            }
        }
    }

    public void replaceMenuById(MenuItem toReplace) {
        synchronized (subMenuItems) {
            replaceMenuById(findParent(toReplace), toReplace);
        }
    }

    public void replaceMenuById(SubMenuItem subMenu, MenuItem toReplace) {
        synchronized (subMenuItems) {
            ArrayList<MenuItem> list = subMenuItems.get(subMenu);
            int idx = -1;
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i).getId() == toReplace.getId()) {
                    idx = i;
                }
            }

            if (idx != -1) {
                MenuItem oldItem = list.set(idx, toReplace);
                if (toReplace.hasChildren()) {
                    ArrayList<MenuItem> items = subMenuItems.remove(oldItem);
                    subMenuItems.put(toReplace, items);
                }
            }

        }
    }

    public void moveItem(SubMenuItem parent, MenuItem newItem, MoveType moveType) {
        synchronized (subMenuItems) {
            ArrayList<MenuItem> items = subMenuItems.get(parent);
            int idx = items.indexOf(newItem);
            if(idx < 0) return;

            items.remove(idx);

            idx = (moveType == MoveType.MOVE_UP)? --idx : ++idx;
            if(idx<0) idx=0;

            if(idx>=items.size()) {
                items.add(newItem);
            }
            else {
                items.add(idx, newItem);
            }
        }
    }

    public void removeMenuItem(MenuItem toRemove) {
        synchronized (subMenuItems) {
            removeMenuItem(findParent(toRemove), toRemove);
        }
    }

    public SubMenuItem findParent(MenuItem toFind) {
        synchronized (subMenuItems) {
            SubMenuItem parent = MenuTree.ROOT;
            for (Map.Entry<MenuItem, ArrayList<MenuItem>> entry : subMenuItems.entrySet()) {
                for (MenuItem item : entry.getValue()) {
                    if (item.getId() == toFind.getId()) {
                        parent = MenuItemHelper.asSubMenu(entry.getKey());
                    }
                }
            }
            return parent;
        }
    }

    public void removeMenuItem(SubMenuItem parent, MenuItem item) {
        SubMenuItem subMenu = (parent != null) ? parent : ROOT;

        synchronized (subMenuItems) {
            ArrayList<MenuItem> subMenuChildren = subMenuItems.get(subMenu);
            if (subMenuChildren == null) {
                throw new UnsupportedOperationException("Menu element not found");
            }

            subMenuChildren.remove(item);
            if (item.hasChildren()) {
                subMenuItems.remove(item);
            }
        }
        menuStates.remove(item.getId());
    }

    public Set<MenuItem> getAllSubMenus() {
        synchronized (subMenuItems) {
            return subMenuItems.keySet();
        }
    }

    public ImmutableList<MenuItem> getMenuItems(MenuItem item) {
        synchronized (subMenuItems) {
            ArrayList<MenuItem> menuItems = subMenuItems.get(item);
            return menuItems == null ? null : ImmutableList.copyOf(menuItems);
        }
    }

    public void changeItem(MenuItem item, MenuState<?> menuState) {
        menuStates.put(item.getId(), menuState);
    }

    public MenuState getMenuState(MenuItem item) {
        return menuStates.get(item.getId());
    }
}
