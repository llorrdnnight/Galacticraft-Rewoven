package com.hrznstudio.galacticraft.blocks.machines;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.impl.SimpleFixedItemInv;
import com.google.common.collect.Lists;
import com.hrznstudio.galacticraft.api.configurable.SideOptions;
import com.hrznstudio.galacticraft.api.item.EnergyHolderItem;
import com.hrznstudio.galacticraft.energy.GalacticraftEnergy;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public abstract class MachineBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    public static final int DEFAULT_MAX_ENERGY = 15000;

    public SimpleEnergyAttribute energy = new SimpleEnergyAttribute(getMaxEnergy(), GalacticraftEnergy.GALACTICRAFT_JOULES);
    private SimpleFixedItemInv inventory = new SimpleFixedItemInv(getInvSize());

    public String owner = "";
    public boolean isParty = false;
    public boolean isPublic = true;

    public String redstoneOption = "DISABLED";
    
    public SideOptions[] config = {SideOptions.BLANK, SideOptions.BLANK, SideOptions.BLANK, SideOptions.BLANK, SideOptions.BLANK, SideOptions.BLANK};

    public MachineBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
        this.energy.listen(this::markDirty);
    }

    public boolean isActive() {
        if (this.getWorld().isReceivingRedstonePower(pos) && redstoneOption.equals("OFF")) {
            return false;
        } else return this.getWorld().isReceivingRedstonePower(pos) || !redstoneOption.equals("ON");
    }

    /**
     * The max energy that this machine can hold. Override for machines that should hold more.
     *
     * @return Energy capacity of this machine.
     */
    protected int getMaxEnergy() {
        return DEFAULT_MAX_ENERGY;
    }

    public SimpleEnergyAttribute getEnergy() {
        return energy;
    }

    // Tries charging the block entity with the given itemstack
    protected void attemptChargeFromStack(ItemStack battery) {
        if (GalacticraftEnergy.isEnergyItem(battery)) {
            int itemEnergy = GalacticraftEnergy.getBatteryEnergy(battery);
            EnergyHolderItem item = (EnergyHolderItem) battery.getItem();

            if (itemEnergy > 0 && energy.getCurrentEnergy() < energy.getMaxEnergy()) {
                int energyToRemove = 5;
                int amountFailedToInsert = item.extract(battery, energyToRemove);
                energy.insertEnergy(GalacticraftEnergy.GALACTICRAFT_JOULES, energyToRemove - amountFailedToInsert, Simulation.ACTION);
            }
        }
    }

    protected abstract int getInvSize();

    public final SimpleFixedItemInv getInventory() {
        return inventory;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("Energy", getEnergy().getCurrentEnergy());
        tag.put("Inventory", inventory.toTag());
        tag.putString("Owner", owner);
        tag.putBoolean("Party", isParty);
        tag.putBoolean("Public", isPublic);
        tag.putString("Redstone", redstoneOption);
        tag.putString("Config", config[0].name() + "," + config[1].name() + "," + config[2].name() + "," + config[3].name() + "," + config[4].name() + "," + config[5].name());
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        getEnergy().setCurrentEnergy(tag.getInt("Energy"));
        inventory.fromTag(tag.getCompound("Inventory"));
        owner = tag.getString("Owner");
        isParty = tag.getBoolean("Party");
        isPublic = tag.getBoolean("Public");
        redstoneOption = tag.getString("Redstone");
        
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return this.toTag(tag);
    }
}
