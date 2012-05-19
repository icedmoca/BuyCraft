package de.minestar.buycraft.core;

import org.bukkit.plugin.PluginManager;

import de.minestar.buycraft.listener.ActionListener;
import de.minestar.buycraft.manager.DatabaseManager;
import de.minestar.buycraft.manager.ItemManager;
import de.minestar.buycraft.manager.ShopManager;
import de.minestar.minestarlibrary.AbstractCore;

public class Core extends AbstractCore {
    public static final String NAME = "BuyCraft";

    /**
     * Manager
     */
    private DatabaseManager databaseManager;

    @Override
    protected boolean commonDisable() {
        if (this.databaseManager.hasConnection())
            this.databaseManager.closeConnection();
        return true;
    }

    private ShopManager shopManager;
    private ItemManager itemManager;

    /**
     * Listener
     */
    private ActionListener guardListener;

    public Core() {
        this(NAME);
    }

    public Core(String name) {
        super(NAME);
    }

    @Override
    protected boolean createManager() {
        this.databaseManager = new DatabaseManager(Core.NAME, getDataFolder());
        if (!this.databaseManager.hasConnection())
            return false;

        this.shopManager = new ShopManager(this.databaseManager);
        this.itemManager = new ItemManager();

        return true;
    }

    @Override
    protected boolean createListener() {
        this.guardListener = new ActionListener(this.shopManager, this.itemManager, this.databaseManager);
        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(this.guardListener, this);
        return true;
    }
}