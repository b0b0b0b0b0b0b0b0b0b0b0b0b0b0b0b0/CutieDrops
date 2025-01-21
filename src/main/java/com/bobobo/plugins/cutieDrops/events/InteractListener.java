package com.bobobo.plugins.cutieDrops.events;
import com.bobobo.plugins.cutieDrops.CutieDrops;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
public class InteractListener implements Listener {
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!(event.getRightClicked() instanceof Ageable mob)) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.SHEARS) return;
        EntityType mobType = mob.getType();
        Material dropMaterial = CutieDrops.getInstance()
                .getConfigManager()
                .getDropMaterial(mobType);
        if (dropMaterial == null) {
            return;
        }
        if (!mob.isAdult()) {
            if (CutieDrops.getInstance().getConfigManager().isEnableMessages()) {
                player.sendMessage(CutieDrops.getInstance().getMessage("mob_already_baby"));
            }
            return;
        }
        mob.setBaby();
        mob.getWorld().dropItem(mob.getLocation(), new ItemStack(dropMaterial));
        if (CutieDrops.getInstance().getConfigManager().isEnableMessages()) {
            player.sendMessage(CutieDrops.getInstance().getMessage("mob_transformed"));
        }
    }
}
