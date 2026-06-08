package com.t.compactcombat.combat.weapon;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class WeaponManager {

    private static final WeaponData SWORD = new WeaponData(WeaponType.SWORD, 1.0F, 1.0F, 1.0F, 3.0F, 3, 8);
    private static final WeaponData GREATSWORD = new WeaponData(WeaponType.GREATSWORD, 1.5F, 1.5F, 0.8F, 3.5F, 2,
            14);
    private static final WeaponData DAGGER = new WeaponData(WeaponType.DAGGER, 0.7F, 0.6F, 1.5F, 2.5F, 4, 5);
    private static final WeaponData SPEAR = new WeaponData(WeaponType.SPEAR, 1.1F, 1.2F, 0.9F, 4.0F, 3, 10);
    private static final WeaponData KATANA = new WeaponData(WeaponType.KATANA, 1.2F, 1.0F, 1.2F, 3.2F, 4, 7);

    public static WeaponData getCurrentWeapon(Player player) {
        Item item = player.getMainHandItem().getItem();

        if (item == Items.IRON_SWORD) {
            return SWORD;
        }
        if (item == Items.NETHERITE_SWORD) {
            return GREATSWORD;
        }
        if (item == Items.STICK) {
            return SPEAR;
        }
        if (item == Items.GOLDEN_SWORD) {
            return KATANA;
        }

        return SWORD;
    }
}
